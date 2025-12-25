import { Link } from "react-router-dom";
const Footer = () => {
  // Kullanıcı giriş durumunu kontrol et
  const customer = JSON.parse(sessionStorage.getItem("active-customer"));
  const admin = JSON.parse(sessionStorage.getItem("active-admin"));
  const isLoggedIn = customer !== null || admin !== null;

  return (
    <div>
      <div class="container my-5">
        <footer class="text-center text-lg-start">
          <div class="container-fluid p-4 pb-0">
            <section class="">
              <div class="row">
                <div class="col-lg-4 col-md-6 mb-4 mb-md-0">
                  <h4 class="text-uppercase text-color">
                    <i>ARAÇ KİRALAMA SİSTEMİ</i>
                  </h4>

                  <p className="header-logo-color">
                    Merhaba, ben Abdulkadir. Hayalini kurduğumuz Rent A Car platformunu gerçeğe dönüştürerek sizlere kolay, güvenilir ve kullanıcı dostu bir araç kiralama deneyimi sunmayı amaçladık. Keşfetmenin keyfini çıkarın, yolculuğunuza güvenle başlayın. Hoş geldiniz!
                  </p>
                </div>

              </div>
            </section>

            <hr class="mb-4" />

            {/* Sadece giriş yapmamış kullanıcılara "Giriş Yap" butonu göster */}
            {!isLoggedIn && (
              <section class="">
                <p class="d-flex justify-content-center align-items-center">
                  <span class="me-3 custom-bg-text">
                    <b>Buradan Giriş Yapın</b>
                  </span>
                  <Link to="/user/login" class="active">
                    <button
                      type="button"
                      class="btn btn-outline-light btn-rounded bg-color custom-bg-text"
                    >
                      <b> Giriş Yap</b>
                    </button>
                  </Link>
                </p>
              </section>
            )}

            <hr class="mb-4" />
          </div>

          <div class="text-center text-color">

          </div>
        </footer>
      </div>
    </div>
  );
};

export default Footer;
