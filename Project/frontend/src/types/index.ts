export interface User {
  id: number;
  username: string;
  email: string;
  fullName: string;
  phoneNumber?: string;
  role: 'ADMIN' | 'BROKER' | 'CLIENT';
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  type: string;
  id: number;
  username: string;
  email: string;
  role: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  email: string;
  fullName: string;
  phoneNumber?: string;
  role: 'ADMIN' | 'BROKER' | 'CLIENT';
}

export interface Client {
  id: number;
  fullName: string;
  dateOfBirth: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  phoneNumber: string;
  email: string;
  occupation?: string;
  education?: string;
  religion?: string;
  caste?: string;
  subcaste?: string;
  annualIncome?: number;
  city?: string;
  state?: string;
  country?: string;
  heightCm?: number;
  weightKg?: number;
  maritalStatus: 'NEVER_MARRIED' | 'DIVORCED' | 'WIDOWED' | 'SEPARATED';
  
  // Family Information
  fatherName?: string;
  fatherOccupation?: string;
  motherName?: string;
  motherOccupation?: string;
  familyType?: string;
  familyIncome?: number;
  siblingsCount?: number;
  
  // Preferences
  preferredAgeMin?: number;
  preferredAgeMax?: number;
  preferredHeightMin?: number;
  preferredHeightMax?: number;
  preferredReligion?: string;
  preferredCaste?: string;
  preferredEducation?: string;
  preferredOccupation?: string;
  preferredIncomeMin?: number;
  preferredLocation?: string;
  
  // Profile
  profilePhotoUrl?: string;
  horoscopeUrl?: string;
  bio?: string;
  isActive: boolean;
  
  // Metadata
  brokerId: number;
  brokerName: string;
  createdAt: string;
  updatedAt: string;
  age?: number;
}

export interface ClientRegistrationRequest {
  fullName: string;
  dateOfBirth: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  phoneNumber: string;
  email: string;
  occupation?: string;
  education?: string;
  religion?: string;
  caste?: string;
  subcaste?: string;
  annualIncome?: number;
  city?: string;
  state?: string;
  country?: string;
  heightCm?: number;
  weightKg?: number;
  maritalStatus: 'NEVER_MARRIED' | 'DIVORCED' | 'WIDOWED' | 'SEPARATED';
  
  // Family Information
  fatherName?: string;
  fatherOccupation?: string;
  motherName?: string;
  motherOccupation?: string;
  familyType?: string;
  familyIncome?: number;
  siblingsCount?: number;
  
  // Preferences
  preferredAgeMin?: number;
  preferredAgeMax?: number;
  preferredHeightMin?: number;
  preferredHeightMax?: number;
  preferredReligion?: string;
  preferredCaste?: string;
  preferredEducation?: string;
  preferredOccupation?: string;
  preferredIncomeMin?: number;
  preferredLocation?: string;
  
  // Profile
  profilePhotoUrl?: string;
  horoscopeUrl?: string;
  bio?: string;
  brokerId: number;
}