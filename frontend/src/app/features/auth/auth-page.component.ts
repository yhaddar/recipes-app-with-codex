import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthApiService } from './auth-api.service';
import { AuthStorageService } from '../../core/services/auth-storage.service';

@Component({
  selector: 'rm-auth-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <section class="auth-shell shell">
      <div>
        <p class="eyebrow">Welcome back</p>
        <h1>{{ mode === 'login' ? 'Come back to the table.' : 'Make room for your recipes.' }}</h1>
        <p class="lede">Your recipes, plans, and kitchen notes in one calm place.</p>
      </div>
      <form class="auth-card panel" [formGroup]="form" (ngSubmit)="submit()" novalidate>
        <div class="auth-switch" role="tablist" aria-label="Authentication mode">
          <button type="button" [class.active]="mode === 'login'" (click)="setMode('login')">Sign in</button>
          <button type="button" [class.active]="mode === 'register'" (click)="setMode('register')">Register</button>
        </div>
        <label class="field">Email<input type="email" formControlName="email" autocomplete="email" /></label>
        <label *ngIf="mode === 'register'" class="field">First name<input formControlName="firstName" autocomplete="given-name" /></label>
        <label *ngIf="mode === 'register'" class="field">Last name<input formControlName="lastName" autocomplete="family-name" /></label>
        <label class="field">Password<input type="password" formControlName="password" autocomplete="current-password" /></label>
        <p *ngIf="error" class="form-error" role="alert">{{ error }}</p>
        <p *ngIf="message" class="form-success" role="status">{{ message }}</p>
        <button class="button button-primary submit" type="submit" [disabled]="form.invalid || submitting">
          {{ submitting ? 'Working…' : mode === 'login' ? 'Sign in' : 'Create account' }}
        </button>
      </form>
    </section>
  `,
  styles: `
    .auth-shell { align-items: center; display: grid; gap: 64px; grid-template-columns: 1fr 0.8fr; min-height: 680px; }
    .auth-shell h1 { font-size: clamp(3rem, 6vw, 5.5rem); line-height: 1; margin: 18px 0; }
    .auth-card { display: grid; gap: 16px; background: var(--linen); }
    .auth-switch { display: flex; gap: 8px; margin-bottom: 8px; }
    .auth-switch button { background: transparent; border: 0; border-bottom: 2px solid transparent; cursor: pointer; padding: 10px 2px; }
    .auth-switch button + button { margin-left: 16px; }
    .auth-switch button.active { border-color: var(--terracotta); font-weight: 800; }
    .field { display: grid; gap: 7px; font-size: 0.85rem; font-weight: 700; }
    input { border: 1px solid var(--border); border-radius: 8px; font: inherit; padding: 12px; }
    .submit { border: 0; cursor: pointer; margin-top: 8px; }
    .submit:disabled { cursor: not-allowed; opacity: 0.55; }
    .form-error { color: var(--terracotta); margin: 0; }
    .form-success { color: var(--leaf); margin: 0; }
    @media (max-width: 720px) { .auth-shell { gap: 24px; grid-template-columns: 1fr; min-height: auto; padding-block: 48px; } }
  `,
})
export class AuthPageComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly authApi = inject(AuthApiService);
  private readonly storage = inject(AuthStorageService);

  readonly form = this.formBuilder.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
    firstName: [''],
    lastName: [''],
  });
  mode: 'login' | 'register' = 'login';
  submitting = false;
  error = '';
  message = '';

  setMode(mode: 'login' | 'register'): void {
    this.mode = mode;
    this.error = '';
    this.message = '';
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting = true;
    this.error = '';
    const value = this.form.getRawValue();
    const request = this.mode === 'login'
      ? this.authApi.login({ email: value.email, password: value.password })
      : this.authApi.register({ email: value.email, password: value.password, firstName: value.firstName, lastName: value.lastName });
    request.subscribe({
      next: (response) => {
        this.submitting = false;
        this.message = response.message ?? (this.mode === 'login' ? 'Login successful' : 'Registration received');
        if (this.mode === 'login') {
          this.storage.saveToken((response.data as { sessionToken: string }).sessionToken);
        }
      },
      error: () => {
        this.submitting = false;
        this.error = 'We could not complete that request. Please check your details and try again.';
      },
    });
  }
}
