#version 330

uniform vec2 resolution;
uniform float currentTime;
uniform vec3 camPos;
uniform vec3 camDir;
uniform vec3 camUp;
uniform sampler2D tex;
uniform bool showStepDepth;

in vec3 pos;

out vec3 color;

#define PI 3.1415926535897932384626433832795
#define RENDER_DEPTH 800
#define CLOSE_ENOUGH 0.00001

#define BACKGROUND -1
#define BALL 0
#define BASE 1

#define GRADIENT(pt, func) vec3( \
    func(vec3(pt.x + 0.0001, pt.y, pt.z)) - func(vec3(pt.x - 0.0001, pt.y, pt.z)), \
    func(vec3(pt.x, pt.y + 0.0001, pt.z)) - func(vec3(pt.x, pt.y - 0.0001, pt.z)), \
    func(vec3(pt.x, pt.y, pt.z + 0.0001)) - func(vec3(pt.x, pt.y, pt.z - 0.0001)))

const vec3 LIGHT_POS[] = vec3[](vec3(5, 18, 10));

///////////////////////////////////////////////////////////////////////////////

vec3 getBackground(vec3 dir) {
  float u = 0.5 + atan(dir.z, -dir.x) / (2 * PI);
  float v = 0.5 - asin(dir.y) / PI;
  vec4 texColor = texture(tex, vec2(u, v));
  return texColor.rgb;
}

vec3 getRayDir() {
  vec3 xAxis = normalize(cross(camDir, camUp));
  return normalize(pos.x * (resolution.x / resolution.y) * xAxis + pos.y * camUp + 5 * camDir);
}

///////////////////////////////////////////////////////////////////////////////

float sphere(vec3 pt) {
  return length(pt) - 1;
}

float cube(vec3 p) {
    vec3 d = abs(p) - vec3(1); // 1 = radius
    return min(max(d.x, max(d.y, d.z)), 0.0) + length(max(d, 0.0));
}

float sphereUnionCube(vec3 p) {
    return min(cube(p), sphere(p-vec3(1,0,1)));
}

float sphereDifferenceCube(vec3 p) {
    return max(cube(p), -sphere(p-vec3(1,0,1)));
}

float sphereIntersectCube(vec3 p) {
    return max(cube(p), sphere(p-vec3(1,0,1)));
}

float smin(float a, float b) {
    float k = 0.2;
    float h = clamp(0.5 + 0.5 * (b - a) / k, 0,1);
    return mix(b, a, h) - k * h * (1 - h);
}

float sphereSmoothCube(vec3 p) {
    return smin(cube(p), sphere(p-vec3(1,0,1)));
}

float torus(vec3 p) {
    vec2 t = vec2(3,0.5);
    vec2 q = vec2(length(p.xz) - t.x, p.y);
    return length(q) - t.y;
}

float getShapes(vec3 p) {
    //LETS JUST RANDOMLY CHANGE THE HEIGHT REQUIREMENT WITHOUT TELLING ANYONE
    p -= vec3(0,0,4);
    vec3 p1 = vec3(mod(p.x,8), p.y, mod(p.z+4,8)-4);
    vec3 p2 = vec3(mod(p.x+4,8)-4, p.y, mod(p.z,8));
    vec3 p3 = vec3(mod(p.x+4,8)-4, p.y, mod(p.z+4,8)-4);
    mat3 t1 = mat3(
        vec3(1,0,0),
        vec3(0,0,1),
        vec3(0,1,0)
    );
    mat3 t2 = mat3(
        vec3(0,1,0),
        vec3(1,0,0),
        vec3(0,0,1)
        );
    float torus1 = torus(p1*inverse(t1)-vec3(4,0,0));
    float torus2 = torus(p2*inverse(t2)-vec3(0,0,4));
    float torus3 = torus(p3);
    return min(torus1, min(torus2, torus3));
}

float getPlane(vec3 p) {
    return p.y + 1.0;
}

float getSDF(vec3 p) {
    return min(getShapes(p), getPlane(p));
}

vec3 getNormal(vec3 pt) {
  return normalize(GRADIENT(pt, getSDF));
}

vec3 getColor(vec3 pt) {
    //more efficient to check if plane or shape elsewhere, but this is prettier
    float dist = getShapes(pt);
    if (getPlane(pt) < dist) {
        if (mod(dist, 5) > 4.75) {
            return vec3(0,0,0);
        }
        return mix(vec3(0.4,1,0.4), vec3(0.4,0.4,1), mod(dist, 1));
    }
    return vec3(1);
}

///////////////////////////////////////////////////////////////////////////////

float shadow(vec3 pt, vec3 l, float length) {
    float kd = 1;
    int step = 0;
    for (float t = 0.1;t < length && step < RENDER_DEPTH && kd > 0.001; ) {
        float d = abs(getSDF(pt + t * l));
        if (d < 0.001) {
            kd = 0;
        } else {
            kd = min(kd, 16 * d / t);
        }
        t += d;
        step++;
    }
    return kd;
}

float shade(vec3 eye, vec3 pt, vec3 n) {
  float val = 0;
  float ka = 0.1;
  float kd = 1.0;
  float ks = 1.0;
  float a = 256;

  val += ka;  // Ambient
  
  for (int i = 0; i < LIGHT_POS.length(); i++) {
    vec3 l = normalize(LIGHT_POS[i] - pt);

    float subval = 0;
    subval += kd*max(dot(n, l), 0); //Diffuse

    vec3 r = normalize(2*dot(l,n)*n-l);
    vec3 v = normalize(eye-pt);
    subval += ks*pow(max(dot(r,v),0),a); //Specular

    //Shadows
    float sha = shadow(pt, l, length(LIGHT_POS[i]-pt));

    val += sha*subval;
  }
  return val;
}

vec3 illuminate(vec3 camPos, vec3 rayDir, vec3 pt) {
  vec3 c, n;
  n = getNormal(pt);
  c = getColor(pt);
  return shade(camPos, pt, n) * c;
}

///////////////////////////////////////////////////////////////////////////////

vec3 raymarch(vec3 camPos, vec3 rayDir) {
  int step = 0;
  float t = 0;

  for (float d = 1000; step < RENDER_DEPTH && abs(d) > CLOSE_ENOUGH; t += abs(d)) {
    d = getSDF(camPos + t * rayDir);
    step++;
  }

  if (step == RENDER_DEPTH) {
    return getBackground(rayDir);
  } else if (showStepDepth) {
    return vec3(float(step) / RENDER_DEPTH);
  } else {
    return illuminate(camPos, rayDir, camPos + t * rayDir);
  }
}

///////////////////////////////////////////////////////////////////////////////

void main() {
  color = raymarch(camPos, getRayDir());
}