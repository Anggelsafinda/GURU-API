# 🌐 CORS Configuration Guide - Multi-Device Access

Panduan lengkap untuk mengkonfigurasi akses API dari multiple device tanpa hardcode IP address.

---

## 📋 Daftar Isi
1. [Konsep CORS](#konsep-cors)
2. [Konfigurasi Dasar](#konfigurasi-dasar)
3. [Untuk Satu Device](#untuk-satu-device)
4. [Untuk Multiple Device](#untuk-multiple-device)
5. [Menggunakan Environment Variable](#menggunakan-environment-variable)
6. [Troubleshooting](#troubleshooting)

---

## 🔍 Konsep CORS

**CORS (Cross-Origin Resource Sharing)** adalah mekanisme keamanan browser yang memastikan hanya website/aplikasi yang terdaftar yang bisa mengakses API.

**Tanpa CORS**, browser akan block semua request dari origin berbeda dengan aturan **Same-Origin Policy**.

**Contoh**:
- ✅ Frontend di `http://localhost:5173` bisa akses API di `http://localhost:8082` → DIIZINKAN
- ❌ Frontend di `http://192.168.1.10:5173` bisa akses API di `http://localhost:8082` → BLOCKED (tanpa CORS)

---

## ⚙️ Konfigurasi Dasar

### File: `src/main/resources/application.properties`

```properties
# CORS Allowed Origins: Daftar URL yang diizinkan mengakses API
# Format: comma-separated list (tanpa spasi di antara URL)
cors.allowed.origins=http://localhost:5173,http://localhost:8000,http://localhost:8082
```

### Struktur Property
- **Property Name**: `cors.allowed.origins`
- **Format**: URL comma-separated
  - ✅ Benar: `http://localhost:5173,http://192.168.1.10:3000,http://192.168.1.10:8000`
  - ❌ Salah: `http://localhost:5173, http://192.168.1.10:3000` (ada spasi → error)
  
---

## 🖥️ Untuk Satu Device (Localhost)

### Skenario
- Backend berjalan di: `http://localhost:8082`
- Frontend berjalan di: `http://localhost:5173`

### Konfigurasi
```properties
cors.allowed.origins=http://localhost:5173,http://localhost:8082
```

---

## 📱 Untuk Multiple Device

### Skenario
- **Server/Backend**: `192.168.1.100:8082` (IP address server)
- **Device Frontend 1**: `192.168.1.10` (smartphone, tablet, laptop)
- **Device Frontend 2**: `192.168.1.20` (device testing lain)
- **Localhost Development**: `localhost:5173` (development machine)

### Langkah-Langkah

#### 1️⃣ Cari IP Address Server Backend

**Windows (Command Prompt)**:
```cmd
ipconfig
```
Cari `IPv4 Address` di interface yang sesuai (biasanya `192.168.x.x`)

**Linux/Mac**:
```bash
ifconfig
# atau
ip addr show
```

Contoh output: `192.168.1.100`

#### 2️⃣ Cari IP Address Setiap Device Frontend

**Windows Command Prompt**:
```cmd
ipconfig
```

**Android Device**:
```
Settings > About phone > IP address
```

**iOS Device**:
```
Settings > Wi-Fi > (nama WiFi) > IP Address
```

#### 3️⃣ Update `application.properties`

```properties
# Format: http://IP:PORT,http://IP:PORT,...
cors.allowed.origins=\
  http://localhost:5173,\
  http://localhost:8082,\
  http://192.168.1.100:8082,\
  http://192.168.1.10:3000,\
  http://192.168.1.10:8000,\
  http://192.168.1.20:5173,\
  http://192.168.1.30:8080
```

#### 4️⃣ Restart Backend
```bash
./mvnw spring-boot:run
```

#### 5️⃣ Test dari Device
Buka browser/frontend di device dengan URL:
```
http://192.168.1.10:3000
```

---

## 🔐 Menggunakan Environment Variable

### Alasan
- Tidak perlu edit `application.properties` di setiap environment
- Lebih fleksibel untuk development vs production
- Lebih aman (tidak hardcode semua IP di version control)

### Syntax

#### Windows PowerShell
```powershell
# Set environment variable
$env:CORS_ALLOWED_ORIGINS="http://localhost:5173,http://192.168.1.10:3000,http://192.168.1.20:5173"

# Run Spring Boot
.\mvnw spring-boot:run
```

#### Windows Command Prompt (cmd)
```cmd
# Set environment variable
set CORS_ALLOWED_ORIGINS=http://localhost:5173,http://192.168.1.10:3000

# Run Spring Boot
mvnw spring-boot:run
```

#### Linux/Mac Bash
```bash
# Set environment variable
export CORS_ALLOWED_ORIGINS="http://localhost:5173,http://192.168.1.10:3000,http://192.168.1.20:5173"

# Run Spring Boot
./mvnw spring-boot:run
```

#### Permanent Environment Variable (Windows)
1. Buka `View advanced system settings`
2. Klik `Environment Variables...`
3. Klik `New...` di "User variables"
4. **Variable name**: `CORS_ALLOWED_ORIGINS`
5. **Variable value**: `http://localhost:5173,http://192.168.1.10:3000,...`
6. Klik `OK` dan restart terminal/IDE

---

## 📋 Contoh Konfigurasi Lengkap

### Application.properties
```properties
# ==================== MySQL Database Configuration ====================
spring.datasource.url=jdbc:mysql://localhost:3306/guru_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update

# ==================== Server Configuration ====================
server.address=0.0.0.0
server.port=8082

# ==================== CORS Configuration - EDIT INI UNTUK MULTI-DEVICE ====================
# Daftar URL yang diizinkan mengakses API
# Format: comma-separated value (TANPA SPASI)
cors.allowed.origins=http://localhost:5173,http://localhost:8000,http://localhost:8082,http://192.168.1.100:8082

# ==================== Logging ====================
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## 🧪 Testing CORS

### Method 1: Dengan Postman
1. Open Postman
2. New Request → **GET** → `http://localhost:8082/api/teachers`
3. Set Authorization: Basic Auth
   - Username: `admin`
   - Password: `admin123`
4. Klik **Send** → Harusnya OK (HTTP 200)

### Method 2: Frontend Error Checking
Jika error di browser console:
```
Access to XMLHttpRequest at 'http://localhost:8082/api/teachers' 
from origin 'http://192.168.1.10:3000' has been blocked by CORS policy
```

**Artinya**: `http://192.168.1.10:3000` belum di-add ke `cors.allowed.origins`

**Solusi**:
```properties
cors.allowed.origins=http://localhost:5173,http://192.168.1.10:3000
# Kemudian restart backend
```

### Method 3: Curl Testing
```bash
curl -u admin:admin123 \
  -H "Origin: http://192.168.1.10:3000" \
  http://localhost:8082/api/teachers
```

Respons header akan berisi:
```
Access-Control-Allow-Origin: http://192.168.1.10:3000
```

---

## 🔧 Troubleshooting

### 1️⃣ "CORS Error" - Origin not allowed
**Error**:
```
Access to XMLHttpRequest at 'http://192.168.1.100:8082/api/teachers' 
from origin 'http://192.168.1.10:3000' has been blocked by CORS policy
```

**Solusi**:
```properties
# Add IP device ke cors.allowed.origins
cors.allowed.origins=http://localhost:5173,http://192.168.1.10:3000
```

### 2️⃣ "Connection refused" - Tidak bisa connect ke backend
**Error**: `ERR_CONNECTION_REFUSED`

**Penyebab**: Backend tidak running atau port salah

**Solusi**:
1. Verifikasi backend sudah running: `netstat -ano | findstr :8082`
2. Verify IP address: `ipconfig` (harus `0.0.0.0` atau actual IP)
3. Verify firewall tidak block port 8082

### 3️⃣ "401 Unauthorized"
**Penyebab**: Username/password salah

**Solusi**:
```properties
# Default credentials
Username: admin
Password: admin123
```

### 4️⃣ Device tidak bisa akses backend (network issue)
**Error**: `Connection timeout`

**Solusi**:
1. Verify device terhubung ke same WiFi/network
2. Ping backend IP: `ping 192.168.1.100`
3. Verify IP address device: `ipconfig` (device)
4. Verify firewall: `netstat -bo` (check port 8082 listening)

---

## 📌 Checklist Multi-Device Setup

- [ ] Backend running: `./mvnw spring-boot:run`
- [ ] Backend IP diketahui: `ipconfig` → IPv4 Address
- [ ] Device IP diketahui: `ipconfig` di device
- [ ] CORS origins di-update di `application.properties`
- [ ] Format format correct (comma-separated, no spaces)
- [ ] Backend di-restart setelah update CORS
- [ ] Device bisa ping backend IP
- [ ] Frontend URL di-update dengan backend IP
- [ ] Test dari device: buka frontend di `http://device-ip:port`
- [ ] Check browser console untuk CORS errors
- [ ] Basic Auth credentials: `admin` / `admin123`

---

## 🚀 Quick Start for Production

Untuk production environment, gunakan environment variable:

```bash
# Set all allowed origins via single environment variable
set CORS_ALLOWED_ORIGINS=http://app.example.com,https://app.example.com,https://staging.app.com

# Run backend
java -jar target/GURU-API-0.0.1-SNAPSHOT.jar
```

---

## 📖 Referensi
- [Spring CORS Guide](https://spring.io/guides/gs/enabling-cors/)
- [MDN: CORS Documentation](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS)
- [RFC 7231: HTTP CORS](https://tools.ietf.org/html/rfc7231#section-6)

---

**Last Updated**: March 9, 2026
**Backend Version**: GURU-API 0.0.1
