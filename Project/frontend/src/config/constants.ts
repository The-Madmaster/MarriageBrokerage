// API Base Configuration
export const API_BASE_URL = 'http://localhost:8080/api';

// API Endpoints
export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: '/auth/login',
    REGISTER: '/auth/register',
  },
  BROKER: {
    CLIENTS: '/broker/clients',
    ADD_CLIENT: '/broker/clients',
  },
  ADMIN: {
    BROKERS: '/admin/brokers',
  },
};

// Local Storage Keys
export const STORAGE_KEYS = {
  TOKEN: 'token',
  USER: 'user',
};

// User Roles
export const USER_ROLES = {
  ADMIN: 'ADMIN',
  BROKER: 'BROKER',
  CLIENT: 'CLIENT',
} as const;

// Routes
export const ROUTES = {
  HOME: '/',
  LOGIN: '/login',
  REGISTER: '/register',
  DASHBOARD: '/dashboard',
  CLIENTS: '/clients',
  BROKERS: '/brokers',
  PROFILE: '/profile',
} as const;