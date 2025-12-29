import { useState, useEffect } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import { useLocation } from "react-router-dom";
import axios from "axios";

const UserProfile = () => {
  const location = useLocation();
  var customer = location.state;

  const sessionCustomer = JSON.parse(sessionStorage.getItem("active-customer"));

  const [user, setUser] = useState(customer);

  let navigate = useNavigate();

  const addDrivingLicense = (booking) => {
    navigate("/customer/driving-license/add");
  };

  const retrieveUser = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/user/fetch/user-id?userId=" + customer.id,
      {
        headers: {
          Authorization: "Bearer " + sessionStorage.getItem("customer-jwtToken"),
        },
      }
    );
    return response.data;
  };

  useEffect(() => {
    const getUser = async () => {
      const res = await retrieveUser();
      if (res) {
        setUser(res.users[0]);
      }
    };

    getUser();
  }, []);

  return (
    <div>
      {/* User Profile Card */}
      <div className="d-flex align-items-center justify-content-center ms-5 mt-1 me-5 mb-3">
        <div
          className="card form-card rounded-card h-100 custom-bg"
          style={{
            width: "900px",
          }}
        >
          <div className="card-body header-logo-color">
            <h4 className="card-title text-color  text-center">
              Kışisel Bilgiler
            </h4>

            <div className="row mt-4">
              <div className="col-md-4">
                <p className="mb-2">
                  <b className="text-color">Ad:</b> {user.firstName}
                </p>
              </div>
              <div className="col-md-4">
                <p className="mb-2">
                  <b className="text-color">Soyad:</b> {user.lastName}
                </p>
              </div>
              <div className="col-md-4">
                <p className="mb-2">
                  <b className="text-color">Email:</b> {user.emailId}
                </p>
              </div>
            </div>
            <div className="row mt-2">
              <div className="col-md-4">
                <p className="mb-2">
                  <b className="text-color">Telefon Numarası:</b> {user.phoneNo}
                </p>
              </div>
              <div className="col-md-4">
                <p className="mb-2">
                  <b className="text-color">Adres:</b>{" "}
                  {user.address.street +
                    " " +
                    user.address.city +
                    " " +
                    user.address.pincode}
                </p>
              </div>
            </div>
            <h4 className="card-title text-color  text-center mt-5">
              Sürücü Belgesi
            </h4>
            {(() => {
              if (user.license) {
                return (
                  <div>
                    <div className="row mt-4">
                      <div className="col-md-4">
                        <p className="mb-2">
                          <b className="text-color">Sürücü Belgesi Numarası:</b>{" "}
                          {user.license.licenseNumber}
                        </p>
                      </div>
                      <div className="col-md-4">
                        <p className="mb-2">
                          <b className="text-color">Son Kullanım Tarihi:</b>{" "}
                          {user.license.expirationDate}
                        </p>
                      </div>

                      <div class="d-flex aligns-items-center justify-content-center mt-3">
                        <img
                          src={
                            "http://localhost:8080/api/user/" +
                            user.license.licensePic
                          }
                          className="card-img-top rounded img-fluid"
                          alt="variant img"
                          style={{
                            maxWidth: "350px",
                            display: "inline-block",
                          }}
                        />
                      </div>
                    </div>
                  </div>
                );
              } else if (
                sessionCustomer &&
                sessionCustomer.role === "Customer"
              ) {
                return (
                  <div className="d-flex aligns-items-center justify-content-center">
                    <button
                      onClick={(e) => addDrivingLicense()}
                      className="btn btn-md bg-color custom-bg-text mt-4 "
                    >
                      <b>Sürücü Belgesi Ekle</b>
                    </button>
                  </div>
                );
              } else {
                return (
                  <div className="text-center header-logo-color mt-4">
                    <h5>Yüklenmedi</h5>
                  </div>
                );
              }
            })()}
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserProfile;
