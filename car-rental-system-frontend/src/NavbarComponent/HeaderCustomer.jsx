import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const HeaderCustomer = () => {
  const navigate = useNavigate();

  const user = JSON.parse(sessionStorage.getItem("active-customer"));

  const userLogout = (e) => {
    e.preventDefault(); // 🔴 Link default davranışını durdur

    toast.success("Çıkış yapıldı", {
      position: "top-center",
      autoClose: 1000,
    });

    // 🔴 TÜM CUSTOMER OTURUMUNU TEMİZLE
    sessionStorage.removeItem("active-customer");
    sessionStorage.removeItem("customer-jwtToken");

    // 🔴 KISA BEKLE → SONRA YÖNLENDİR
    setTimeout(() => {
      navigate("/", { replace: true });
      window.location.reload(); // 🔴 GARANTİ
    }, 1000);
  };

  const viewProfile = () => {
    navigate("/user/profile/detail", { state: user });
  };

  return (
      <ul className="navbar-nav ms-auto mb-2 mb-lg-0 me-5">
        <li className="nav-item">
          <Link
              to="/customer/bookings"
              className="nav-link active"
          >
            <b className="text-color">Rezervasyonlarım</b>
          </Link>
        </li>

        <li className="nav-item">
        <span
            className="nav-link active"
            style={{ cursor: "pointer" }}
            onClick={viewProfile}
        >
          <b className="text-color">Profilim</b>
        </span>
        </li>

        <li className="nav-item">
        <span
            className="nav-link active"
            style={{ cursor: "pointer" }}
            onClick={userLogout}
        >
          <b className="text-color">Çıkış Yap</b>
        </span>
        </li>

        <ToastContainer />
      </ul>
  );
};

export default HeaderCustomer;
