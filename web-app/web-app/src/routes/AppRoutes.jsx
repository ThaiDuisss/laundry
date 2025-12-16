import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "../pages/Login";
import Home from "../pages/Home";
import Profile from "../pages/Profile";
import Chat from "../pages/Chat";
import ProductDetail from "../pages/ProductDetail";
import RegisterForm from "../pages/RegisterPage/RegisterForm";
import AwaitEmailConfirmation from "../components/AwaitEmailConfirmation";
import CartPage from "../pages/Cart";

const AppRoutes = () => {
  return (
    <Router>
      <Routes>
        {" "}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<RegisterForm />} />
        <Route path="/" element={<Home />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/chat" element={<Chat />} />
        <Route path="/await" element={<AwaitEmailConfirmation />} />
        <Route path="/product/:id" element={<ProductDetail />} />
        <Route path="/cart" element={<CartPage />} />
      </Routes>
    </Router>
  );
};

export default AppRoutes;
