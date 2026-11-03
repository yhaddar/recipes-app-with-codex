import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { authInterceptor } from './auth.interceptor';
import { AuthStorageService } from '../services/auth-storage.service';
import { ApiClientService } from '../services/api-client.service';

describe('authInterceptor', () => {
  it('adds the bearer token to protected API requests', () => {
    TestBed.configureTestingModule({
      providers: [
        ApiClientService,
        AuthStorageService,
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
      ],
    });
    TestBed.inject(AuthStorageService).saveToken('test-token');
    TestBed.inject(ApiClientService).get('/recipes').subscribe();

    const request = TestBed.inject(HttpTestingController).expectOne('http://localhost:8080/api/recipes');
    expect(request.request.headers.get('Authorization')).toBe('Bearer test-token');
    request.flush({});
  });
});
