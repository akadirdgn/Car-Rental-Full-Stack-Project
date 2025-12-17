import { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import creditcard from "../images/credit-card.png";
import { ToastContainer, toast } from "react-toastify";

const BookingPayment = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const booking = location.state;

  // ✅ Hook HER ZAMAN en üstte
  const [paymentRequest, setPaymentRequest] = useState({
    bookingId: null,
    nameOnCard: "",
    cardNo: "",
    cvv: "",
    expiryDate: "",
  });

  // 🔴 booking yoksa geri dön
  useEffect(() => {
    if (!booking) {
      navigate("/customer/bookings");
    }
  }, [booking, navigate]);

  // ✅ booking GELDİKTEN SONRA bookingId set et
  useEffect(() => {
    if (booking) {
      setPaymentRequest((prev) => ({
        ...prev,
        bookingId: booking.bookingId ?? booking.id,
      }));
    }
  }, [booking]);

  const handleUserInput = (e) => {
    setPaymentRequest({
      ...paymentRequest,
      [e.target.name]: e.target.value,
    });
  };

  const payAndConfirmBooking = (e) => {
    e.preventDefault();

    if (!paymentRequest.bookingId) {
      toast.error("Booking bilgisi eksik");
      return;
    }

    fetch("http://localhost:8080/api/booking/customer/payment", {
      method: "PUT",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(paymentRequest),
    })
        .then((res) => res.json())
        .then((res) => {
          if (res.success) {
            toast.success(res.responseMessage, {
              position: "top-center",
              autoClose: 2000,
            });

            setTimeout(() => {
              navigate("/customer/bookings");
            }, 2000);
          } else {
            toast.error(res.responseMessage, {
              position: "top-center",
              autoClose: 1500,
            });
          }
        })
        .catch(() => {
          toast.error("It seems server is down", {
            position: "top-center",
            autoClose: 1500,
          });
        });
  };

  // 🔴 booking yokken güvenli render
  if (!booking) {
    return (
        <div className="text-center mt-5">
          <h4>Ödeme bilgisi bulunamadı</h4>
        </div>
    );
  }

  return (
      <div className="d-flex justify-content-center mt-4">
        <div className="card form-card rounded-card custom-bg" style={{ maxWidth: "900px" }}>
          <div className="card-body header-logo-color">
            <h4 className="text-center">Payment Details</h4>

            <div className="row mt-4">
              <div className="col-sm-4">
                <img src={creditcard} className="img-fluid" alt="card" />
              </div>

              <div className="col-sm-6">
                <form className="row g-3" onSubmit={payAndConfirmBooking}>
                  <input
                      type="text"
                      className="form-control"
                      name="nameOnCard"
                      placeholder="Name Surname"
                      onChange={handleUserInput}
                      value={paymentRequest.nameOnCard}
                      required
                  />

                  <input
                      type="text"
                      className="form-control"
                      name="cardNo"
                      placeholder="0000 0000 0000 0000"
                      onChange={handleUserInput}
                      value={paymentRequest.cardNo}
                      required
                  />

                  <input
                      type="text"
                      className="form-control"
                      name="expiryDate"
                      placeholder="01/24"
                      onChange={handleUserInput}
                      value={paymentRequest.expiryDate}
                      required
                  />

                  <input
                      type="number"
                      className="form-control"
                      name="cvv"
                      placeholder="123"
                      onChange={handleUserInput}
                      value={paymentRequest.cvv}
                      required
                  />

                  <button type="submit" className="btn bg-color custom-bg-text">
                    PAY {booking.totalPrice}₺
                  </button>

                  <ToastContainer />
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
  );
};

export default BookingPayment;
