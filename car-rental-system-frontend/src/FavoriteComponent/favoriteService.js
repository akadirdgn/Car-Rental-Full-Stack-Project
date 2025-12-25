import axios from "axios";
import { API_BASE_URL } from "../apiConfig";

class FavoriteService {
    addToFavorites(favoriteRequest) {
        return axios.post(API_BASE_URL + "/favorite/add", favoriteRequest);
    }

    getFavoritesByCustomer(customerId) {
        return axios.get(API_BASE_URL + "/favorite/fetch/user-wise?customerId=" + customerId);
    }

    checkIfFavorited(carId, customerId) {
        return axios.get(API_BASE_URL + "/favorite/check?carId=" + carId + "&customerId=" + customerId);
    }

    removeFromFavorites(carId, customerId) {
        return axios.delete(API_BASE_URL + "/favorite/remove?carId=" + carId + "&customerId=" + customerId);
    }
}

export default new FavoriteService();
