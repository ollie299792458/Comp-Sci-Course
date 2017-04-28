//
// Very simple plotter code - DJ Greaves.
// PlotUtil.cs
//
using System;
using System.IO;
using System.Text;
using System.Windows.Forms;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Drawing.Text;


namespace PlotUtil
{

  public class Plotter
  {
    Graphics g;
    bool opened = false;
    uint Width, Height;

    public Plotter()
    {
//       Width = w; Height = h;
    }


  static uint scale = 1;

    System.Drawing.Pen [] pens = new System.Drawing.Pen[6];

  public void Open(uint w, uint h)
  {
    Console.WriteLine("Opened {0} {1}", w, h);
    Width = w; Height = h;
    Form f = new Form ();
    f.Width = (int)(w*scale);
    f.Height = (int)(h*scale);
    f.Visible = true;
    g = f.CreateGraphics();
    g.Clear(System.Drawing.Color.White);
    f.Activate(); 
    pens[0] = new System.Drawing.Pen(Color.Black, 3);
    pens[1] = new System.Drawing.Pen(Color.White, 3);
    pens[2] = new System.Drawing.Pen(Color.Blue, 1);
    pens[3] = new System.Drawing.Pen(Color.FromArgb(255, 0, 33), 1);
    pens[4] = new System.Drawing.Pen(Color.FromArgb(255, 255, 33), 1);
    pens[5] = new System.Drawing.Pen(Color.FromArgb(90, 0, 230), 1);

    opened = true;
  } 

  public void Clear() {     g.Clear(System.Drawing.Color.White); }

  uint clip(uint x)
       {  if (x<0) return 0U;
          if (x>= Height) return Height-1U;
	  return x;
       }


  public void DrawPoint(uint x, uint y, int color)
  {
//    Console.WriteLine("Mark {0} {1} {2}", x, y, color);
      if (!opened) Open(Width, Height);
      g.DrawRectangle(pens[color], x, y, 2, 2);      
  }

    public void Set_pixel(uint x, uint y, int color)
    {
      //    Console.WriteLine("Mark {0} {1} {2}", x, y, color);
      if (!opened) Open(Width, Height);
      g.DrawRectangle(pens[color], x, y, 4, 3);      
    }

    public void MakeXAxis (uint y)
    {
      if (!opened) Open(Width, Height);
      g.DrawRectangle(pens[2], 0, y, Width, 0);      
    }


  }


  public class WavFile
  {
    uint numsamples = 44100;
    uint numchannels = 1;
    uint samplelength = 2; // in bytes
    uint samplerate = 44100;
    int min = -128;
    int max = 127;
    int clips = 0;
    int s = 0;

    BinaryWriter wr;
    public WavFile(uint frames, uint precision)
    {
      numsamples = frames;
      samplelength = precision / 8;
      if (precision == 16)
      {
	max = 32767;
      }
      min = -max - 1;
    }

    public void write_hdr(string fn)
    {
	FileStream f = new FileStream(fn, FileMode.Create);
	wr = new BinaryWriter(f);

	wr.Write(Encoding.ASCII.GetBytes("RIFF"));
	wr.Write(0);
	wr.Write(Encoding.ASCII.GetBytes("WAVE"));
	wr.Write(Encoding.ASCII.GetBytes("fmt "));
	wr.Write((int)16);
	wr.Write((short)1); // Encoding PCM
	wr.Write((short)numchannels); // Channels
	wr.Write((int)(samplerate)); // Sample rate
	wr.Write((int)(samplerate * samplelength * numchannels)); // Average bytes per second
	wr.Write((short)(samplelength * numchannels)); // block align
	wr.Write((short)(8 * samplelength)); // bits per sample
	// wr.Write((short)(numsamples * samplelength)); // Extra size
	wr.Write(Encoding.ASCII.GetBytes("data"));
     }

     public void write_val(int v)
     {
       if (v > max) { v = max; clips ++; }
       if (v < min) { v = min; clips ++; }
       s++;
       if (samplelength == 2)
       {
	  wr.Write((byte)((v >> 0) & 0xff)); // 16 bit LE
	  wr.Write((byte)((v >> 8) & 0xff)); // 
       }
       else wr.Write((byte)((v + 128) & 0xff)); // 8bit
     }

     public void write_test()
     {
       double t = 0.0;
       for (int i = 0; i < numsamples; i++, t += 1.0 / samplerate)
       {
	  write_val(i & 0xff);
       }
       Close();
     }

     public void Close() { 
       wr.Close(); 
       if (clips != 0) Console.WriteLine("Range [{1} .. {2}]: Clips {0}", clips, min, max);
       Console.WriteLine("Wrote {0} frames - target was {1}", s, numsamples);
     }
  }
}
// eof
