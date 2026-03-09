# 📚 CORS Configuration Examples (Contoh-Contoh Real-World)

Contoh konfigurasi CORS untuk berbagai skenario penggunaan.

---

## Skenario 1: Local Development (Hanya Localhost)

**Situasi**: Developer hanya testing di laptop, tanpa multi-device

```properties
# application.properties
cors.allowed.origins=http://localhost:5173,http://localhost:8000,http://localhost:8082
```

**Akses Frontend**: http://localhost:5173

---

## Skenario 2: Multi-Device dalam Satu WiFi

**Situasi**:
- Backend Server (bisa diakses dari device lain): `192.168.1.5:8082`
- Device 1 (Web Frontend): `192.168.1.10:3000`
- Device 2 (Mobile App via WebView): `192.168.1.20:8000`
- Development Laptop: `localhost:5173`

```properties
# application.properties
cors.allowed.origins=\
  http://localhost:5173,\
  http://localhost:8000,\
  http://localhost:8082,\
  http://192.168.1.5:8082,\
  http://192.168.1.10:3000,\
  http://192.168.1.20:8000
```

**Testing**:
- Dari Dev Laptop: `http://localhost:5173` → ✅ OK
- Dari Device 1: `http://192.168.1.10:3000` (akses backend: `http://192.168.1.5:8082`) → ✅ OK
- Dari Device 2: `http://192.168.1.20:8000` (akses backend: `http://192.168.1.5:8082`) → ✅ OK

---

## Skenario 3: Production dengan Single Domain

**Situasi**: Deploy ke production dengan domain name

```properties
# application.properties
cors.allowed.origins=https://app.guru.com,https://admin.guru.com
```

**Catatan**:
- HTTPS endpoint (production best practice)
- Domain based, tidak perlu IP address
- Frontend di `app.guru.com` bisa akses API di `app.guru.com`
- Admin panel di `admin.guru.com` juga authorized

---

## Skenario 4: Multi-Domain Production

**Situasi**: API di-share ke multiple external domain

```properties
# application.properties
cors.allowed.origins=\
  https://app.guru.com,\
  https://admin.guru.com,\
  https://partner1.example.com,\
  https://partner2.example.com
```

**Penjelasan**:
- Strict whitelist: hanya domain yang terdaftar bisa akses
- Setiap partner punya domain sendiri
- Lebih aman daripada wildcard `*`

---

## Skenario 5: Menggunakan Environment Variable (Recommended untuk Production)

### Setup Windows
```powershell
# PowerShell - Set di startup script
$env:CORS_ALLOWED_ORIGINS="https://app.guru.com,https://admin.guru.com,https://partner.example.com"
.\mvnw spring-boot:run
```

### Setup Linux/Docker
```bash
#!/bin/bash
# startup.sh

export CORS_ALLOWED_ORIGINS="https://app.guru.com,https://admin.guru.com"

java -jar target/GURU-API-0.0.1-SNAPSHOT.jar
```

### Docker Compose
```yaml
version: '3.8'
services:
  backend:
    image: guru-api:latest
    ports:
      - "8082:8082"
    environment:
      - CORS_ALLOWED_ORIGINS=https://app.guru.com,https://admin.guru.com,https://partner.example.com
    depends_on:
      - mysql

  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_DATABASE=guru_db
      - MYSQL_ROOT_PASSWORD=admin123
```

---

## Skenario 6: Development dengan Vite + Multiple Port

**Situasi**: Frontend development dengan Vite [localhost:5173 + hot reload], backend [localhost:8082]

```properties
cors.allowed.origins=\
  http://localhost:3000,\
  http://localhost:5173,\
  http://localhost:8000,\
  http://localhost:8080,\
  http://localhost:8082
```

**Alasan**:
- Vite development server di port 5173 (default)
- Fallback ports jika ada conflict
- Testing dengan berbagai port

---

## Skenario 7: Wildcard (⚠️ Security Alert - Jangan di Production)

```properties
# application.properties - HANYA UNTUK DEVELOPMENT!
cors.allowed.origins=*
```

**⚠️ WARNING**:
- Mengizinkan **SEMUA** origin mengakses API
- **SANGAT TIDAK AMAN** untuk production
- Hanya untuk development/proof-of-concept
- Exposed ke CSRF attack

**Alternatif yang lebih aman**: Gunakan explicit whitelist

```properties
# LEBIH BAIK
cors.allowed.origins=http://localhost:5173,http://localhost:8082,http://192.168.1.10:3000
```

---

## Skenario 8: Testing dengan Postman + Device

**Situasi**: Testing API dari Postman dan juga dari device

```properties
# application.properties
cors.allowed.origins=\
  http://localhost:5173,\
  http://localhost:8082,\
  http://192.168.1.100:5173
```

**Testing**:

### Postman (Desktop)
1. New Request
2. Method: GET
3. URL: `http://localhost:8082/api/teachers`
4. Auth: Basic Auth (admin / admin123)
5. Send → ✅ No CORS error (Postman tidak enforce CORS policy)

### Device Web Browser
1. Open device browser
2. URL: `http://192.168.1.100:5173`
3. Do something yang request ke API
4. Request akan include Origin header: `Origin: http://192.168.1.100:5173`
5. Server akan check jika origin di whitelist
6. Jika ada → ✅ Allow, Jika tidak → ❌ Block

---

## Skenario 9: CI/CD Pipeline dengan Dynamic CORS

**Situasi**: Setiap deployment environment (dev, staging, prod) punya allowed origins berbeda

### .env file approach
```bash
# .env.development
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:8082

# .env.staging  
CORS_ALLOWED_ORIGINS=https://staging-app.guru.com,https://staging-api.guru.com

# .env.production
CORS_ALLOWED_ORIGINS=https://app.guru.com,https://api.guru.com
```

### GitHub Actions example
```yaml
name: Deploy to Staging

on:
  push:
    branches: [staging]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      
      - name: Deploy to Server
        env:
          CORS_ALLOWED_ORIGINS: https://staging-app.guru.com,https://staging-api.guru.com
        run: |
          ./deploy.sh
```

---

## Skenario 10: Mobile App dengan WebView + API Server

**Situasi**: 
- Native mobile app with WebView (Android/iOS)
- Backend API di separate server
- Need CORS untuk WebView cross-origin requests

```properties
# application.properties
cors.allowed.origins=\
  http://localhost:5173,\
  capacitor://localhost,\
  ionic://localhost,\
  file://
```

**Explanation**:
- `capacitor://localhost` - Ionic/Capacitor development
- `ionic://localhost` - Ionic app protocol
- `file://` - Local file access (mobile app)

---

## 🧪 Quick Testing Commands

### Windows Batch
```batch
@echo off
REM Reset and set new CORS origins
set CORS_ALLOWED_ORIGINS=http://localhost:5173,http://192.168.1.10:3000
echo CORS configured: %CORS_ALLOWED_ORIGINS%
call mvnw spring-boot:run
```

### PowerShell
```powershell
$env:CORS_ALLOWED_ORIGINS="http://localhost:5173,http://192.168.1.10:3000"
.\mvnw spring-boot:run
```

### Linux/Mac
```bash
#!/bin/bash
export CORS_ALLOWED_ORIGINS="http://localhost:5173,http://192.168.1.10:3000"
./mvnw spring-boot:run
```

---

## 📊 Comparison Table

| Skenario | Approach | Aman? | Fleksibel? |
|----------|----------|-------|-----------|
| Dev Lokal | application.properties | ✅ | ⭐ |
| Multi-Device WiFi | application.properties | ✅ | ⭐⭐ |
| Production | Environment Variable | ✅✅ | ⭐⭐⭐ |
| External Partners | Explicit Whitelist | ✅✅ | ⭐⭐ |
| Wildcard * | Simple | ❌ | ⭐⭐⭐ |

---

## ✅ Best Practices

1. **Development**: Explicit list di application.properties
2. **Production**: Environment variable atau ConfigServer
3. **Multi-domain**: Whitelist semua authorized domain
4. **Never**: Jangan use wildcard `*` di production
5. **Testing**: Use Postman untuk test tanpa CORS overhead
6. **Monitoring**: Log all CORS requests untuk security audit

---

**Updated**: March 9, 2026
