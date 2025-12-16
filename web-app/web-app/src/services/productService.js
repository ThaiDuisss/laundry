import httpClient from "../configurations/httpClient";
import { API } from "../configurations/configuration";
import { getToken } from "./localStorageService";

export const getProducts = async ({
  page = 0,
  size = 5,
  category,
  keyword,
} = {}) => {
  return await httpClient.get(API.MY_PRODUCT, {
    params: {
      page,
      size,
      category: category != null ? category : 0,
      keyword: keyword != null ? keyword : "",
    },
  });
};

export const getProductById = async (id) => {
  return await httpClient.get(`${API.ITEM_DETAIL}/${id}`);
};
export const createPost = async (content) => {
  return await httpClient.post(
    API.CREATE_POST,
    { content: content },
    {
      headers: {
        Authorization: `Bearer ${getToken()}`,
        "Content-Type": "application/json",
      },
    }
  );
};
