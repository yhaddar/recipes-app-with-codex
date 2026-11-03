import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthStorageService } from '../services/auth-storage.service';

export const authGuard: CanActivateFn = () => {
  const storage = inject(AuthStorageService);
  if (storage.isAuthenticated) {
    return true;
  }
  return inject(Router).createUrlTree(['/auth']);
};
