import axiosInstance from './axiosInstance';

export const fetchTopGainers = async () => {
  try {
    const response = await axiosInstance.get('/');
    return response.data;
  } catch (error) {
    console.error('Error fetching top gainers:', error);
    throw error;
  }
};

export const warmUpBackend = async () => {
    return axiosInstance.get("/");
  };
  
