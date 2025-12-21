import React, { useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";

const ViewCustomerBooking = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const booking = location.state;

  useEffect(() => {
    if (!booking) {
      navigate("/admin/bookings");
    }
  }, [booking, navigate]);

  if (!booking) {
    return (
      <div className="text-center mt-5">
        <h4>Rezervasyon bilgisi bulunamadı</h4>
      </div>
    );
  }

  const formatDateFromEpoch = (epochTime) => {
    const date = new Date(Number(epochTime));
    return date.toLocaleString();
  };

  return (
    <div className="container-fluid">
      <div className="row">

        <div className="col-sm-4 mt-2">
          <div className="card form-card custom-bg">
            <img
              src={`http://localhost:8080/api/variant/${booking.variant?.image}`}
              className="card-img-top rounded img-fluid"
              alt="araç"
            />
          </div>
        </div>

        <div className="col-sm-4 mt-2">
          <div className="card form-card custom-bg shadow-lg">
            <div className="card-header bg-color custom-bg-text">
              <h3 className="card-title">{booking.variant?.name}</h3>
            </div>

            <div className="card-body text-color">
              <h5>Firma : <span className="header-logo-color">{booking.variant?.company?.name}</span></h5>
              <h5>Model Numarası : <span className="header-logo-color">{booking.variant?.modelNumber}</span></h5>
              <h5>Yakıt Türü : <span className="header-logo-color">{booking.variant?.fuelType}</span></h5>
              <h5>Günlük Ücret : <span className="header-logo-color">{booking.variant?.pricePerDay}₺</span></h5>
              <h5>Araç Plakası : <span className="header-logo-color">{booking.vehicle?.registrationNumber || "Yok"}</span></h5>
              <h5>Rezervasyon Durumu : <span className="header-logo-color">
                {booking.status === "Approved" ? "Onaylandı" :
                  booking.status === "Pending" ? "Beklemede" :
                    booking.status === "Cancelled" ? "İptal Edildi" :
                      booking.status === "Rejected" ? "Reddedildi" :
                        booking.status === "Paid & Confirmed" ? "Ödendi & Onaylandı" : booking.status}
              </span></h5>
            </div>
          </div>
        </div>

        <div className="col-sm-4 mt-2">
          <div className="card form-card custom-bg shadow-lg">
            <div className="card-header bg-color custom-bg-text text-center">
              <h3>Rezervasyon Bilgileri</h3>
            </div>

            <div className="card-body text-color">
              <h5>Rezervasyon No : <span className="header-logo-color">{booking.bookingId}</span></h5>
              <h5>Rezervasyon Tarihi : <span className="header-logo-color">{formatDateFromEpoch(booking.bookingTime)}</span></h5>
              <h5>Başlangıç Tarihi : <span className="header-logo-color">{booking.startDate}</span></h5>
              <h5>Bitiş Tarihi : <span className="header-logo-color">{booking.endDate}</span></h5>
              <h5>Toplam Tutar : <span className="header-logo-color">{booking.totalPrice}₺</span></h5>
              <h5>Ödeme Durumu : <span className="header-logo-color">{booking.payment ? "Ödendi" : "Beklemede"}</span></h5>
            </div>
          </div>
        </div>
      </div>

      <div className="row mt-3">

        <div className="col-sm-4">
          <div className="card form-card custom-bg shadow-lg">
            <div className="card-header bg-color custom-bg-text">
              <h3>Müşteri Bilgileri</h3>
            </div>

            <div className="card-body text-color">
              <h5>Ad Soyad : <span className="header-logo-color">{booking.customer?.firstName} {booking.customer?.lastName}</span></h5>
              <h5>Telefon : <span className="header-logo-color">{booking.customer?.phoneNo}</span></h5>
              <h5>E-posta : <span className="header-logo-color">{booking.customer?.emailId}</span></h5>
              <h5>
                Adres :
                <span className="header-logo-color">
                  {booking.customer?.address
                    ? `${booking.customer.address.street} ${booking.customer.address.city} ${booking.customer.address.pincode}`
                    : "Bilgi yok"}
                </span>
              </h5>
            </div>
          </div>
        </div>

        <div className="col-sm-4">
          <div className="card form-card custom-bg shadow-lg">
            <div className="card-header bg-color custom-bg-text">
              <h3>Sürücü Belgesi Bilgileri</h3>
            </div>

            <div className="card-body text-color">
              <h5>Belge Numarası : <span className="header-logo-color">{booking.customer?.license?.licenseNumber || "Yok"}</span></h5>
              <h5>Son Geçerlilik Tarihi : <span className="header-logo-color">{booking.customer?.license?.expirationDate || "Yok"}</span></h5>

              {booking.customer?.license?.licensePic && (
                <div className="d-flex justify-content-center mt-3">
                  <img
                    src={`http://localhost:8080/api/user/${booking.customer.license.licensePic}`}
                    className="img-fluid rounded"
                    style={{ maxWidth: "250px" }}
                    alt="ehliyet"
                  />
                </div>
              )}
            </div>
          </div>
        </div>

        <div className="col-sm-4">
          <div className="card form-card custom-bg shadow-lg">
            <div className="card-header bg-color custom-bg-text text-center">
              <h3>Ödeme Bilgileri</h3>
            </div>

            <div className="card-body text-color">
              <h5>Ödeme Durumu : <span className="header-logo-color">{booking.payment ? "Ödendi" : "Beklemede"}</span></h5>
              <h5>İşlem Tarihi : <span className="header-logo-color">{booking.payment?.transactionTime ? formatDateFromEpoch(booking.payment.transactionTime) : "Beklemede"}</span></h5>
              <h5>İşlem Referans No : <span className="header-logo-color">{booking.payment?.transactionRefId || "Beklemede"}</span></h5>
              <h5>Ödenen Tutar : <span className="header-logo-color">{booking.payment ? booking.totalPrice : "0.0"}₺</span></h5>
            </div>
          </div>
        </div>

      </div>
    </div>
  );
};

export default ViewCustomerBooking;
