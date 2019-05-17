# Register your third party application on http://developers.monzo.com by logging in with your personal 
# Monzo account. Copy this file into config.py, and enter your credentials into the file.
MONZO_CLIENT_ID = "oauth2client_00009ezWYO5df2vj0CNJj7"
MONZO_CLIENT_SECRET = "mnzconf.YlAh1/OK77rs3Zim2DtzD6ccaYXxXXENSaVR8Nv6w7P10tFrXz2Dfm8JmiVntWuSN0Rz5emb+b1k1mO/CrS+"

# Configurations you should not need to change.
MONZO_OAUTH_HOSTNAME = "auth.monzo.com"
MONZO_API_HOSTNAME = "api.monzo.com"
MONZO_RESPONSE_TYPE = "code"
MONZO_AUTH_GRANT_TYPE = "authorization_code"
MONZO_REFRESH_GRANT_TYPE = "refresh_token"
MONZO_OAUTH_REDIRECT_URI = "http://localhost:8080/oauth2callbackmonzo/" # For receiving the auth code, not currently used.
MONZO_CLIENT_IS_CONFIDENTIAL = True 
# If your application runs on a backend server with client secret hidden from user, it should be registered 
# as confidential and will have the ability to refresh access tokens.
