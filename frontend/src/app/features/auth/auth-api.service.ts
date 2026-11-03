import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiClientService } from '../../core/services/api-client.service';

export interface AuthEnvelope<T> {
  success: boolean;
  data: T;
  message: string | null;
}

export interface RegistrationPayload {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

export interface LoginPayload {
  email: string;
  password: string;
}

export interface LoginData {
  userId: string;
  sessionToken: string;
  accountStatus: string;
}

@Injectable({ providedIn: 'root' })
export class AuthApiService {
  constructor(private readonly api: ApiClientService) {}

  register(payload: RegistrationPayload): Observable<AuthEnvelope<unknown>> {
    return this.api.post('/auth/register', payload);
  }

  login(payload: LoginPayload): Observable<AuthEnvelope<LoginData>> {
    return this.api.post('/auth/login', payload);
  }
}
