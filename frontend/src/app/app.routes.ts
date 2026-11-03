import { Routes } from '@angular/router';
import { PublicLayoutComponent } from './layouts/public-layout/public-layout.component';
import { LandingComponent } from './features/public/landing/landing.component';
import { AuthPageComponent } from './features/auth/auth-page.component';

export const routes: Routes = [
  {
    path: '',
    component: PublicLayoutComponent,
    children: [
      { path: '', component: LandingComponent },
      { path: 'auth', component: AuthPageComponent },
    ],
  },
];
