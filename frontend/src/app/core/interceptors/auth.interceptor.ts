import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthStorageService } from '../services/auth-storage.service';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const token = inject(AuthStorageService).token;
  const publicAuthPath = ['/auth/register', '/auth/login', '/auth/verify-email', '/auth/forgot-password', '/auth/reset-password'];
  if (!token || publicAuthPath.some((path) => request.url.endsWith(path))) {
    return next(request);
  }

  return next(request.clone({ setHeaders: { Authorization: `Bearer ${token}` } }));
};
