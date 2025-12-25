import axios from "axios";
import { API_BASE_URL } from "../apiConfig";

class ReviewService {
    addReview(review) {
        return axios.post(API_BASE_URL + "/review/add", review);
    }

    getAllReviewsByCarId(carId) {
        return axios.get(API_BASE_URL + "/review/fetch/car-wise?carId=" + carId);
    }
}

export default new ReviewService();
