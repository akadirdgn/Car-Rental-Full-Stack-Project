import React, { useState, useEffect } from "react";
import CarCard from "../CarComponent/CarCard";
import favoriteService from "./favoriteService";

const MyFavorites = () => {
    const [favorites, setFavorites] = useState([]);

    const customer = JSON.parse(sessionStorage.getItem("active-customer"));

    useEffect(() => {
        const retrieveFavorites = async () => {
            if (customer) {
                try {
                    const response = await favoriteService.getFavoritesByCustomer(customer.id);
                    // Backend now returns List<Variant> directly
                    setFavorites(response.data);
                } catch (error) {
                    console.error("Error fetching favorites:", error);
                }
            }
        };

        retrieveFavorites();
    }, []);

    return (
        <div className="container-fluid mb-2" style={{ backgroundColor: "#1f1f1f", minHeight: "100vh" }}>
            <div className="text-center text-color mt-3 mb-3">
                <h2>Favori Araçlarım</h2>
            </div>

            <div className="col-md-12 mt-3 mb-5">
                <div className="row row-cols-1 row-cols-md-2 g-4">
                    {favorites.length > 0 ? (
                        favorites.map((variant) => {
                            return <CarCard item={variant} key={variant.id} />;
                        })
                    ) : (
                        <div className="text-center text-white w-100 mt-5">
                            <h4>Henüz favori aracınız yok.</h4>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default MyFavorites;
