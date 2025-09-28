# Security Features

This document outlines the comprehensive security features implemented in the Marriage Brokerage System.

## 🔐 Authentication & Authorization

### JWT + Refresh Tokens
- **Access Tokens**: 24-hour expiration, HS256 signed
- **Refresh Tokens**: 7-day expiration, UUID-based, database-stored
- **Token Rotation**: Refresh endpoint generates new access tokens
- **Token Revocation**: Logout endpoint revokes refresh tokens

**Endpoints:**
- `POST /api/auth/login` - Login with username/password
- `POST /api/auth/refresh` - Refresh access token
- `POST /api/auth/logout` - Revoke refresh token

### Password Security
- **BCrypt hashing** with Spring Security default rounds
- **Password validation** with custom constraints
- **Account lockout** after failed attempts

## 🛡️ Multi-Factor Authentication (MFA)

### TOTP (Time-based One-Time Password)
- **Google Authenticator** compatible
- **30-second time windows** with 3-window tolerance
- **6-digit codes**
- **QR code generation** for easy setup

### Email OTP
- **5-minute expiration**
- **SMTP integration** for email delivery
- **Fallback authentication** method

### Role Restrictions
- **Admin and Broker roles only** can enable MFA
- **Client users** cannot access MFA features

**Endpoints:**
- `GET /api/mfa/status` - Check MFA status
- `POST /api/mfa/setup-totp` - Generate TOTP secret and QR code
- `POST /api/mfa/enable` - Enable MFA with TOTP verification
- `POST /api/mfa/disable` - Disable MFA with TOTP verification
- `POST /api/mfa/send-email-otp` - Send OTP via email

## 🚦 Rate Limiting & Protection

### Rate Limits
- **Login attempts**: 5 per 15 minutes per IP
- **API calls**: 100 per minute per IP
- **IP-based tracking** with X-Forwarded-For support

### Account Lockout
- **5 failed attempts** locks account for 30 minutes
- **Per-user tracking** of failed attempts
- **Automatic unlock** after timeout period
- **Manual unlock** capability for admins

### Brute Force Prevention
- **Rate limiting filter** blocks excessive requests
- **Progressive backoff** for repeated failures
- **IP blacklisting** for persistent attacks

## 🔒 Database Security

### Field Encryption
- **AES-256 encryption** for sensitive data
- **Configurable encryption keys**
- **Field-level encryption** utility

### Connection Security
- **Encrypted connections** in production
- **SQL injection prevention** via JPA/Hibernate
- **Parameter binding** for all queries

## 🌐 Production Security

### HTTPS Configuration
```properties
# Production HTTPS settings
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=${SSL_PASSWORD}
server.ssl.key-store-type=PKCS12
```

### Environment Variables
```bash
# Required for production
MAIL_USERNAME=your-smtp-username
MAIL_PASSWORD=your-smtp-password
ENCRYPTION_KEY=base64-encoded-aes-key
SSL_PASSWORD=your-ssl-keystore-password
```

## 📊 Security Monitoring

### Logging
- **Failed login attempts** logged with IP and username
- **Account lockouts** logged with timestamps
- **MFA events** (setup, enable, disable) logged
- **Rate limit violations** logged

### Metrics
- Failed authentication attempts per IP
- Account lockout frequency
- MFA adoption rates
- API usage patterns

## 🔧 Configuration

### Application Properties
```properties
# JWT Configuration
jwt.secret=your-base64-secret
jwt.expiration=86400000
jwt.refresh.expiration=604800000

# Security Settings
security.max-failed-attempts=5
security.lockout-duration-minutes=30

# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}

# Encryption
app.encryption.key=${ENCRYPTION_KEY}
```

## 👥 Default Users

### Development/Testing
- **admin** / TheMadmaster@011 (ADMIN role)
- **broker1** / broker123 (BROKER role)
- **client1** / client123 (CLIENT role)

### Production Setup
1. Change default passwords immediately
2. Enable MFA for admin and broker accounts
3. Set up proper email configuration
4. Configure SSL certificates
5. Set encryption keys via environment variables

## 🔍 Security Audit Checklist

- [ ] Default passwords changed
- [ ] MFA enabled for privileged accounts
- [ ] HTTPS enabled with valid certificates
- [ ] Email notifications configured
- [ ] Encryption keys rotated and secured
- [ ] Rate limits tuned for production load
- [ ] Security logging and monitoring enabled
- [ ] Database connections encrypted
- [ ] Regular security updates applied

## 🚨 Incident Response

### Account Compromise
1. Immediately revoke all refresh tokens for the user
2. Force password reset
3. Enable MFA if not already active
4. Review access logs for suspicious activity

### Rate Limit Violations
1. Identify source IP and patterns
2. Implement additional blocking if needed
3. Review application logs for attack vectors
4. Update rate limits if legitimate traffic affected

### Database Security
1. Rotate encryption keys regularly
2. Monitor for unauthorized access attempts
3. Backup encrypted data securely
4. Test disaster recovery procedures

## 📈 Performance Considerations

### Rate Limiting
- Uses in-memory caching (Caffeine) with Bucket4j
- Minimal performance impact on normal operations
- Configurable cleanup intervals

### MFA
- TOTP verification is CPU-light
- Email OTP uses async sending
- Minimal database overhead

### Encryption
- Field-level encryption on-demand
- Caching of encryption keys
- Optimized for read-heavy workloads