# 🚀 CI/CD Kurulum ve Yapılandırma Rehberi

Bu rehber, hazırladığımız `Jenkinsfile` ve `docker-compose.yml` dosyalarını kullanarak projenizi Jenkins üzerinde koşturmanız için gereken adımları içerir.

## 1. 🐳 Docker Hazırlığı

Jenkins'in Docker komutlarını çalıştırabilmesi gerekmektedir.

1.  **Docker Desktop'ı Başlatın:** Bilgisayarınızda veya sunucuda Docker'ın çalıştığından emin olun.
2.  **Jenkins & Docker İzni (Linux/Server kullanıyorsanız):** Jenkins kullanıcısının docker grubuna ekli olduğundan emin olun (`sudo usermod -aG docker jenkins`). Windows'ta Jenkins'i bir container içinde çalıştırıyorsanız, Docker socket'ini mount etmeniz gerekir.

## 2. ⚙️ Jenkins Konfigürasyonu

`Jenkinsfile` içinde tanımladığımız araçların (Maven, NodeJS, JDK) Jenkins'te tanımlı olması şarttır.

### A. Gerekli Eklentileri (Plugins) Yükleyin
Jenkins Ana Sayfa -> **Manage Jenkins** -> **Plugins** menüsünden aşağıdaki eklentilerin kurulu olduğundan emin olun:
- **Docker Pipeline** (Docker ile ilgili tüm eklentiler)
- **NodeJS Plugin**
- **Maven Integration**
- **Pipeline: GitHub Groovy Libraries**

### B. Araçları (Global Tools) Tanımlayın
`Jenkinsfile` içindeki isimlendirmelerle (**Birebir Aynı Olmalı**) araçları tanımlayın.
Ana Sayfa -> **Manage Jenkins** -> **Tools**:

1.  **JDK:**
    - **Name:** `JDK 21` (Jenkinsfile'da `jdk 'JDK 21'` olarak geçiyor)
    - **JAVA_HOME:** Eğer otomatik yükleme seçmezseniz, sistemdeki Java 21 yolunu verin.

2.  **Maven:**
    - **Name:** `Maven 3.9.6` (Jenkinsfile'da `maven 'Maven 3.9.6'` olarak geçiyor)
    - **Install automatically:** Seçeneğini işaretleyip versiyon olarak 3.9.6 (veya yakın bir sürüm) seçin.

3.  **NodeJS:**
    - **Name:** `NodeJS 22` (Jenkinsfile'da `nodejs 'NodeJS 22'` olarak geçiyor)
    - **Install automatically:** Seçip NodeJS 22 sürümünü seçin.

## 3. 🆕 Pipeline Projesi Oluşturma

1.  Jenkins Ana Sayfasında **"New Item"** butonuna tıklayın.
2.  Proje ismi verin (örn: `Car-Rental-CI-CD`).
3.  **"Pipeline"** seçeneğini seçip **OK** deyin.

## 4. 🔗 Pipeline Ayarları

Proje yapılandırma sayfasında **Pipeline** başlığına gelin:

1.  **Definition:** `Pipeline script from SCM` seçin.
2.  **SCM:** `Git` seçin.
3.  **Repository URL:** Projenizin GitHub adresini girin (örn: `https://github.com/akadirdgn/Car-Rental-Full-Stack-Project.git`).
4.  **Branch Specifier:** `*/main` veya `*/master` (hangi branch'te çalışıyorsanız).
5.  **Script Path:** Burası çok önemli! Jenkinsfile'ımız backend klasörü içinde olduğu için şu yolu yazın:
    `car-rental-system-backend/Jenkinsfile`

## 5. ▶️ Çalıştırma

1.  Ayarları kaydedin (**Save**).
2.  **"Build Now"** butonuna tıklayın.
3.  **Stage View** üzerinden adımları (Checkout, Build, Unit Tests, E2E Stages) takip edin.

### 🧪 Selenium Testleri Hakkında Not
Bu pipeline, Selenium testlerini "Headless" (tarayıcı arayüzü olmadan) modda çalıştıracak şekilde yapılandırıldı (`E2ETests.java` içinde). Eğer Jenkins'in çalıştığı makinede (veya container'da) **Google Chrome** kurulu değilse E2E testleri hata verebilir.

**Çözüm:**
- Jenkins'i çalıştıran makineye Chrome tarayıcısını yükleyin.
- Veya "Docker Agent" kullanarak testleri içinde Chrome olan bir container'da koşturacak şekilde pipeline'ı güncelleyebiliriz (İlerleyen aşamada isterseniz).
