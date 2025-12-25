import { useState, useEffect } from "react";
import favoriteService from "./favoriteService";
import { toast } from "react-toastify";

const FavoriteButton = ({ carId }) => {
    const [isFavorite, setIsFavorite] = useState(false);

    const customer = JSON.parse(sessionStorage.getItem("active-customer"));

    useEffect(() => {
        if (carId && customer) {
            checkFavoriteStatus();
        }
    }, [carId, customer]);

    const checkFavoriteStatus = async () => {
        try {
            const response = await favoriteService.checkIfFavorited(carId, customer.id);
            setIsFavorite(response.data);
        } catch (error) {
            console.error("Error checking favorite status", error);
        }
    };

    const toggleFavorite = async () => {
        if (!customer) {
            toast.warning("Favorilere eklemek için giriş yapmalısınız.");
            return;
        }

        try {
            if (isFavorite) {
                await favoriteService.removeFromFavorites(carId, customer.id);
                setIsFavorite(false);
                toast.info("Favorilerden çıkarıldı.");
            } else {
                const request = {
                    carId: carId,
                    customerId: customer.id,
                };
                await favoriteService.addToFavorites(request);
                setIsFavorite(true);
                toast.success("Favorilere eklendi!");
            }
        } catch (error) {
            console.error("Error toggling favorite", error);
            toast.error("İşlem sırasında hata oluştu.");
        }
    };

    return (
        <button
            onClick={toggleFavorite}
            className="btn btn-link text-decoration-none"
            style={{ fontSize: "1.5rem", padding: "0" }}
            title={isFavorite ? "Favorilerden Çıkar" : "Favorilere Ekle"}
        >
            {isFavorite ? (
                <span style={{ color: "red" }}>♥</span>
            ) : (
                <span style={{ color: "white" }}>♡</span>
            )}
        </button>
    );
};

export default FavoriteButton;
