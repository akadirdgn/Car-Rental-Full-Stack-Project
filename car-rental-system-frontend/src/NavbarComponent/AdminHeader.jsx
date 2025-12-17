import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const AdminHeader = () => {
  let navigate = useNavigate();

  const user = JSON.parse(sessionStorage.getItem("active-admin"));
  console.log(user);

  const adminLogout = (e) => {
    e.preventDefault();
    toast.success("Çıkış yapıldı", {
      position: "top-center",
      autoClose: 1000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    });
    sessionStorage.removeItem("active-admin");
    sessionStorage.removeItem("admin-jwtToken");

    setTimeout(() => {
      window.location.href = "/home";
    }, 1000);
  };
  return (
    <ul class="navbar-nav ms-auto mb-2 mb-lg-0 me-5">
      <li class="nav-item">
        <Link
          to="/user/admin/register"
          class="nav-link active"
          aria-current="page"
        >
          <b className="text-color">Admin Kaydı</b>
        </Link>
      </li>
      <li class="nav-item">
        <Link
          to="/admin/company/add"
          class="nav-link active"
          aria-current="page"
        >
          <b className="text-color">Şirket Ekle</b>
        </Link>
      </li>
      <li class="nav-item">
        <Link
          to="/admin/variant/add"
          class="nav-link active"
          aria-current="page"
        >
          <b className="text-color">Varyant Ekle</b>
        </Link>
      </li>
      <li class="nav-item">
        <Link
          to="/admin/variant/all"
          class="nav-link active"
          aria-current="page"
        >
          <b className="text-color"> Varyantlar</b>
        </Link>
      </li>

      <li class="nav-item">
        <Link
          to="/admin/customer/bookings"
          class="nav-link active"
          aria-current="page"
        >
          <b className="text-color"> Rezervasyonlar</b>
        </Link>
      </li>

      <li class="nav-item">
        <Link
          to=""
          class="nav-link active"
          aria-current="page"
          onClick={adminLogout}
        >
          <b className="text-color">Çıkış Yap</b>
        </Link>
      </li>
      <ToastContainer />
    </ul>
  );
};

export default AdminHeader;
