// src/pages/CartPage.js
import React, { useEffect, useState } from "react";
import { getCart } from "../services/orderSevice";

const CartPage = () => {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchCart() {
      try {
        const response = await getCart(); // gọi API
        console.log("DATA", response);
        if (response?.data) {
          setItems(response.data); // lấy danh sách sản phẩm từ response.data
        }
      } catch (error) {
        console.error("❌ Lỗi khi lấy giỏ hàng:", error);
      } finally {
        setLoading(false);
      }
    }

    fetchCart();
  }, []);

  // Nếu bạn chưa có quantity, mặc định 1
  const total = items.reduce((sum, item) => sum + item.prices * 1, 0);

  if (loading) return <p className="p-6">Đang tải giỏ hàng...</p>;

  return (
    <div className="max-w-2xl mx-auto mt-8 bg-white p-6 shadow rounded-lg">
      <h1 className="text-2xl font-semibold mb-4">🛒 Giỏ hàng của bạn</h1>

      {items.length === 0 ? (
        <p>Giỏ hàng trống.</p>
      ) : (
        <>
          <ul className="divide-y divide-gray-200">
            {items.map((item) => (
              <li key={item.id} className="py-4 flex items-center">
                <img
                  src={item.avatar}
                  alt={item.name}
                  className="w-16 h-16 object-cover rounded mr-4"
                />
                <div className="flex-1">
                  <h2 className="font-medium">{item.name}</h2>
                  <p className="text-gray-600">
                    Giá: {item.prices.toLocaleString()}đ
                  </p>
                  <p className="text-gray-500 text-sm">Số lượng: 1</p>
                </div>
                <div className="text-right font-semibold">
                  {(item.prices * 1).toLocaleString()}đ
                </div>
              </li>
            ))}
          </ul>

          <div className="text-right mt-4 text-lg font-semibold">
            Tổng cộng: {total.toLocaleString()}đ
          </div>

          <button
            className="w-full mt-6 bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600"
          >
            Thanh toán
          </button>
        </>
      )}
    </div>
  );
};

export default CartPage;
