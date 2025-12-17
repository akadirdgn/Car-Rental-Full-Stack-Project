import React, { useState, useEffect } from "react";
import axios from "axios";
import { ToastContainer, toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import { Button, Modal } from "react-bootstrap";

const AllBookings = () => {
  const [bookings, setBookings] = useState([]);
  const [vehicles, setVehicles] = useState([]);
  const [variantId, setVariantId] = useState(null);

  const [assignBooking, setAssignBooking] = useState(null);
  const [vehicleId, setVehicleId] = useState("");
  const [status, setStatus] = useState("");

  const [showModal, setShowModal] = useState(false);

  const navigate = useNavigate();

  /* ---------------- API ---------------- */

  const retrieveAllBookings = async () => {
    const res = await axios.get(
        "http://localhost:8080/api/booking/fetch/all"
    );
    setBookings(res.data?.bookings || []);
  };

  const retrieveVehiclesByVariant = async (variantId) => {
    if (!variantId) return;

    const res = await axios.get(
        `http://localhost:8080/api/vehicle/fetch/variant-wise?variantId=${variantId}`
    );
    setVehicles(res.data?.vehicles || []);
  };

  /* ---------------- EFFECTS ---------------- */

  useEffect(() => {
    retrieveAllBookings();
  }, []);

  useEffect(() => {
    if (variantId) {
      retrieveVehiclesByVariant(variantId);
    }
  }, [variantId]);

  /* ---------------- HANDLERS ---------------- */

  const assignBookingVehicle = (booking) => {
    if (!booking?.variant?.id) {
      toast.error("Varyant bilgisi bulunamadı");
      return;
    }

    setAssignBooking(booking);
    setVariantId(booking.variant.id);
    setVehicleId("");
    setStatus("");
    setShowModal(true);
  };

  const updateCustomerBookingStatus = (e) => {
    e.preventDefault();

    if (!assignBooking || !status) {
      toast.error("Eksik bilgi");
      return;
    }

    if (status === "Approved" && !vehicleId) {
      toast.error("Araç seçmelisiniz");
      return;
    }

    const payload =
        status === "Rejected"
            ? { bookingId: assignBooking.id, status }
            : { bookingId: assignBooking.id, status, vehicleId };

    fetch("http://localhost:8080/api/booking/update/assign/vehicle", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    })
        .then((res) => res.json())
        .then((res) => {
          if (res.success) {
            toast.success(res.responseMessage);
            setTimeout(() => window.location.reload(), 1000);
          } else {
            toast.error(res.responseMessage);
          }
        })
        .catch(() => toast.error("Sunucu hatası"));
  };

  const viewCustomerBookingDetail = (booking) => {
    navigate("/customer/vehicle/booking/details", { state: booking });
  };

  const formatDateFromEpoch = (epoch) =>
      new Date(Number(epoch)).toLocaleString();

  /* ---------------- JSX ---------------- */

  return (
      <div className="mt-3">
        <ToastContainer />

        <div
            className="card form-card ms-2 me-2 mb-5 custom-bg"
            style={{ height: "45rem" }}
        >
          <div className="card-header bg-color custom-bg-text text-center">
            <h2>Tüm Rezervasyonlar</h2>
          </div>

          <div className="card-body" style={{ overflowY: "auto" }}>
            <div className="table-responsive">
              <table className="table text-center text-color">
                <thead className="table-bordered border-color bg-color custom-bg-text">
                <tr>
                  <th>Varyant</th>
                  <th>Ad</th>
                  <th>Rezervasyon ID</th>
                  <th>Gün</th>
                  <th>Fiyat</th>
                  <th>Müşteri</th>
                  <th>Rezervasyon Zamanı</th>
                  <th>Başlangıç</th>
                  <th>Bitiş</th>
                  <th>Durum</th>
                  <th>Araç</th>
                  <th>Ödeme</th>
                  <th>Aksiyon</th>
                </tr>
                </thead>

                <tbody className="header-logo-color">
                {bookings.map((b) => (
                    <tr key={b.id}>
                      <td>
                        <img
                            src={`http://localhost:8080/api/variant/${b.variant?.image}`}
                            alt="arac"
                            className="img-fluid"
                            style={{ maxWidth: "90px" }}
                        />
                      </td>
                      <td><b>{b.variant?.name}</b></td>
                      <td><b>{b.bookingId}</b></td>
                      <td><b>{b.totalDay}</b></td>
                      <td><b>{b.totalPrice}₺</b></td>
                      <td>
                        <b>{b.customer?.firstName} {b.customer?.lastName}</b>
                      </td>
                      <td><b>{formatDateFromEpoch(b.bookingTime)}</b></td>
                      <td><b>{b.startDate}</b></td>
                      <td><b>{b.endDate}</b></td>
                      <td><b>{b.status}</b></td>
                      <td>
                        <b>{b.vehicle?.registrationNumber || "Yok"}</b>
                      </td>
                      <td><b>{b.payment ? "Ödendi" : "Beklemede"}</b></td>
                      <td>
                        {b.status === "Pending" && (
                            <button
                                onClick={() => assignBookingVehicle(b)}
                                className="btn btn-sm bg-color custom-bg-text me-1"
                            >
                              Güncelle
                            </button>
                        )}
                        <button
                            onClick={() => viewCustomerBookingDetail(b)}
                            className="btn btn-sm bg-color custom-bg-text"
                        >
                          Görüntüle
                        </button>
                      </td>
                    </tr>
                ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>

        {/* ---------------- MODAL ---------------- */}

        <Modal show={showModal} onHide={() => setShowModal(false)}>
          <Modal.Header closeButton className="bg-color custom-bg-text">
            <Modal.Title>Rezervasyon Durumu Güncelle</Modal.Title>
          </Modal.Header>

          <Modal.Body>
            {assignBooking && (
                <form onSubmit={updateCustomerBookingStatus}>
                  <div className="mb-3">
                    <label className="form-label"><b>Rezervasyon ID</b></label>
                    <input
                        className="form-control"
                        value={assignBooking.bookingId}
                        readOnly
                    />
                  </div>

                  <div className="mb-3">
                    <label className="form-label"><b>Durum</b></label>
                    <select
                        className="form-control"
                        onChange={(e) => setStatus(e.target.value)}
                    >
                      <option value="">Durum Seç</option>
                      <option value="Approved">Onaylandı</option>
                      <option value="Rejected">Reddedildi</option>
                    </select>
                  </div>

                  {status === "Approved" && (
                      <div className="mb-3">
                        <label className="form-label"><b>Araç</b></label>
                        <select
                            className="form-control"
                            onChange={(e) => setVehicleId(e.target.value)}
                        >
                          <option value="">Araç Seç</option>
                          {vehicles.map((v) => (
                              <option key={v.id} value={v.id}>
                                {v.registrationNumber}
                              </option>
                          ))}
                        </select>
                      </div>
                  )}

                  <div className="d-flex justify-content-center">
                    <button className="btn bg-color custom-bg-text">
                      Kaydet
                    </button>
                  </div>
                </form>
            )}
          </Modal.Body>

          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)}>
              Kapat
            </Button>
          </Modal.Footer>
        </Modal>
      </div>
  );
};

export default AllBookings;
