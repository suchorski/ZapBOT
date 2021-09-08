# ZapBOT [![Discord Support](https://img.shields.io/discord/826413601518125087.svg)](https://discord.gg/CE7vCvdDQJ)
A powerful Discord BOT (only in Brazilian Portuguese)

# Configuration
## Spring application properties files
Configure:
- Discord Token
- Owner ID
- Log Channel ID
- Youtube Subscribe command secret key
- Bibble Token
- Database configuration
- hCaptcha configuration

<pre>Discord Bot Token can be got from <a href="https://discord.com/developers/applications" alt="Discord developer portal">Discord Developer Portal</a>.</pre>

<pre>Bibble token can be got from <a href="https://github.com/marciovsena/abibliadigital" alt="Digital Bibble">https://github.com/marciovsena/abibliadigital</a>.</pre>

## Optional configuration to enable SSL (Thanks to [spring-boot-starter-acme](https://github.com/csueiras/spring-boot-starter-acme))

This module depends on having `OpenSSL` on the PATH to convert the certificate to PKCS12 format.

1. Add the module to your `pom.xml` file as a dependency.
2. Build your project. 
3. Deploy it to a target machine and point your domain name to the IP address of that machine. LetsEncrypt validates your ownership of the domain by making a callback to the `http://your-domain/.well-known/acme-challenge/{token}` endpoint exposed by this module.
4. Make sure that your server has `openssl` available on its `$PATH`.
5. To activate `spring-boot-starter-acme` and generate a certificate execute:
```
sudo java -Dserver.port=80 -Dacme.enabled=true -Dacme.domain-name=<YOUR_DOMAIN_NAME> -Dacme.accept-terms-of-service=true -jar mysecureapp-0.0.1-SNAPSHOT.jar
```
6. Check your console for a confirmation that the certificate was successfully generated.
7. Stop your application and configure it to make use of the generated certificate:

```
server.port=443
server.ssl.key-store=keystore.p12
server.ssl.key-store-password=password
server.ssl.keyStoreType=PKCS12
```

# Commands
### Owner
- [X] Shutdown
- [X] Coins (add/withdraw)
- [X] Multiplier of XP
- [X] Ban/unban user from BOT

### Administrator
- [X] Define roles name
- [X] Define roles per level (2 for free / 30 for premium)
- [X] Configure Auto Role
- [X] Configure announce message
- [X] Configure birthday message
- [X] Change nickname (Only for premium)

### Moderator
- [X] Block/unblock member from BOT
- [X] Draw in channel
- [X] Channel with only last message per user
- [X] Subscribe YouTube channel for new videos (1 for free / 10 for premium)
- [X] Enable/disable Auto React on guild

### Helper
- [X] Ping
- [X] Vote
- [X] Faster chooser
- [X] Custom reply (3 for free / 50 for premium)
- [X] Statistics (5 for free / 30 for premium)

### Users
#### Free
- [X] Help
- [X] XP System
- [X] Google search
- [X] YouTube search
- [X] Show random cat
- [X] Show random dog
- [X] Get hourly coins
- [X] Show profile
- [X] Top guild users
- [X] Top global users
- [X] Define birthday date
- [X] Bibble

#### Paid
- [X] Auto reaction on every message (Free for PREMIUM)
- [X] Highlighted message
- [X] Set profile color
- [X] Buy background profile
- [X] Flip a coin
- [X] Throw a dice
- [X] Raffle system
- [X] Scratchoff system
- [X] Recommend users

# Future version
- [ ] Trivia game
- [ ] Rent message channel
- [ ] Rent voice channel
- [ ] Improve database queries
- [ ] Audit channel

# Need a help? [![Discord Support](https://img.shields.io/discord/826413601518125087.svg)](https://discord.gg/CE7vCvdDQJ)
Find me on Discord, Zap#1886!