import { Injectable } from '@angular/core';

const SESSION_TOKEN_KEY = 'recipe-manager.session-token';

@Injectable({ providedIn: 'root' })
export class AuthStorageService {
  get token(): string | null {
    return localStorage.getItem(SESSION_TOKEN_KEY);
  }

  get isAuthenticated(): boolean {
    return Boolean(this.token);
  }

  saveToken(token: string): void {
    localStorage.setItem(SESSION_TOKEN_KEY, token);
  }

  clear(): void {
    localStorage.removeItem(SESSION_TOKEN_KEY);
  }
}
