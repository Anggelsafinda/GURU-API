# 🚀 CORS Quick Reference Card

Quick copy-paste solutions untuk konfigurasi CORS.

---

## 1️⃣ Edit `application.properties`

**File**: `src/main/resources/application.properties`

**Default (Localhost Only)**:
```properties
cors.allowed.origins=http://localhost:5173,http://localhost:8000,http://localhost:8082
```

**Multi-Device (Same WiFi)**:
```properties
cors.allowed.origins=\
  http://localhost:5173,\
  http://192.168.1.5:8082,\
  http://192.168.1.10:3000,\
  http://192.168.1.20:5173
```

**Production**:
```properties
cors.allowed.origins=https://app.guru.com,https://admin.guru.com
```

---

## 2️⃣ Find Your IP Address

### Windows
```cmd
ipconfig
```
Look for: `IPv4 Address: 192.168.x.x`

### Linux/Mac
```bash
ifconfig
```

### Device (Mobile/Tablet)
- **Android**: Settings > About phone > IP address
- **iOS**: Settings > Wi-Fi > (network name) > IP Address

---

## 3️⃣ Restart Backend After Changing CORS

```bash
# Option 1: Maven
./mvnw spring-boot:run

# Option 2: Compiled JAR
java -jar target/GURU-API-0.0.1-SNAPSHOT.jar
```

---

## 4️⃣ Test CORS from Postman

1. **New Request**
2. **URL**: `http://localhost:8082/api/teachers`
3. **Method**: GET
4. **Auth**: Basic Auth → Username: `admin`, Password: `admin123`
5. **Send** → Should return HTTP 200

---

## 5️⃣ Test CORS from Device

1. **Get Backend IP**: `ipconfig` → `192.168.x.x`
2. **Add to CORS**: `cors.allowed.origins=...,http://192.168.x.x:8082,...`
3. **Restart Backend**
4. **Open Device Browser**: `http://192.168.x.x:8082`
5. **Check Console**: No CORS errors?

---

## 6️⃣ Common CORS Error & Solution

```
Access to XMLHttpRequest at 'http://192.168.1.100:8082/api/teachers' 
from origin 'http://192.168.1.10:3000' has been blocked by CORS policy
```

**Solution**:
```properties
# Add missing origin
cors.allowed.origins=http://localhost:5173,http://192.168.1.10:3000

# Restart backend
./mvnw spring-boot:run
```

---

## 7️⃣ Environment Variable (Advanced)

### Windows PowerShell
```powershell
$env:CORS_ALLOWED_ORIGINS="http://localhost:5173,http://192.168.1.10:3000"
.\mvnw spring-boot:run
```

### Windows CMD
```cmd
set CORS_ALLOWED_ORIGINS=http://localhost:5173,http://192.168.1.10:3000
mvnw spring-boot:run
```

### Linux/Mac
```bash
export CORS_ALLOWED_ORIGINS="http://localhost:5173,http://192.168.1.10:3000"
./mvnw spring-boot:run
```

---

## 8️⃣ Default Credentials

```
Username: admin
Password: admin123
```

---

## 9️⃣ Checklist Before Testing

- [ ] Backend is running (`./mvnw spring-boot:run`)
- [ ] Backend IP known (`ipconfig`)
- [ ] Device IP known
- [ ] CORS origins updated in `application.properties`
- [ ] Format is correct (comma-separated, no spaces)
- [ ] Backend restarted after CORS change
- [ ] Device can ping backend: `ping 192.168.x.x`
- [ ] Frontend URL updated with backend IP

---

## 🔟 Summary

| Need | What to Do |
|------|-----------|
| One device (localhost) | Default config ✅ |
| Multiple devices (same WiFi) | Add device IP to `cors.allowed.origins` |
| Production | Use environment variable |
| See CORS error? | Check if origin in allowed list |
| Device can't connect? | Check IP address, firewall, WiFi |

---

## 📞 Still Need Help?

1. Check **CORS-CONFIGURATION-GUIDE.md** for detailed explanation
2. Check **CORS-CONFIGURATION-EXAMPLES.md** for real-world examples
3. Open browser DevTools (F12) → Console tab → Look for CORS errors

---

**Last Updated**: March 9, 2026
