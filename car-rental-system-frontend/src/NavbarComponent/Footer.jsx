import { Link } from "react-router-dom";
const Footer = () => {
  return (
    <div>
      <div class="container my-5">
        <footer class="text-center text-lg-start">
          <div class="container-fluid p-4 pb-0">
            <section class="">
              <div class="row">
                <div class="col-lg-4 col-md-6 mb-4 mb-md-0">
                  <h4 class="text-uppercase text-color">
                    <i>Car Rental System</i>
                  </h4>

                  <p className="header-logo-color">
                    Kadir, Nurullah ve Yusuf olarak birlikte yola çıktık ve hayalimizdeki Rent A Car sitesini gerçeğe dönüştürdük. Kolay, güvenilir ve kullanıcı dostu bir araç kiralama deneyimi sunmak için buradayız. Keşfetmenin keyfini çıkarın, yolculuğun tadını çıkarın. Hoş geldiniz!
                  </p>
                </div>

               </div>
               </section>

            <hr class="mb-4" />

            <section class="">
              <p class="d-flex justify-content-center align-items-center">
                <span class="me-3 custom-bg-text">
                  <b>Login from here</b>
                </span>
                <Link to="/user/login" class="active">
                  <button
                    type="button"
                    class="btn btn-outline-light btn-rounded bg-color custom-bg-text"
                  >
                    <b> Log in</b>
                  </button>
                </Link>
              </p>
            </section>

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
