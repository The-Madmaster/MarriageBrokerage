import apiClient from './api';
import { API_ENDPOINTS } from '../config/constants';
import { Client, ClientRegistrationRequest } from '../types';

export const clientService = {
  getClients: async (brokerId: number, page = 0, size = 10): Promise<Client[]> => {
    const response = await apiClient.get(API_ENDPOINTS.BROKER.CLIENTS, {
      params: { brokerId, page, size },
    });
    return response.data;
  },

  addClient: async (clientData: ClientRegistrationRequest): Promise<string> => {
    const response = await apiClient.post(API_ENDPOINTS.BROKER.ADD_CLIENT, clientData);
    return response.data;
  },
};