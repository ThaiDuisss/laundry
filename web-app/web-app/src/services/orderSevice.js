import httpClient from "../configurations/httpClient";
import { API } from "../configurations/configuration";
import { getToken } from "./localStorageService";

export const addCart = async ({ productId, name, prices, avatar } = {}) => {
  const token = getToken();

  return await httpClient.post(
    API.ADD_CART,
    { productId, name, prices, avatar },
    {
      withCredentials: true,
      headers: token ? { Authorization: `Bearer ${token}` } : {},
    }
  );
};

export const getCart = async () => {
  const token = getToken();
  return await httpClient.get(API.GET_CART, {
    withCredentials: true,
    headers: token ? { Authorization: `Bearer ${token}` } : {},
  });
};
