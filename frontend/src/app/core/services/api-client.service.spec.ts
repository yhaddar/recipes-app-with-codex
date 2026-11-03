import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ApiClientService } from './api-client.service';

describe('ApiClientService', () => {
  let service: ApiClientService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ApiClientService, provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ApiClientService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('uses the configured API base URL for requests', () => {
    service.get<{ status: string }>('/health').subscribe((response) => {
      expect(response.status).toBe('UP');
    });

    const request = http.expectOne('http://localhost:8080/api/health');
    expect(request.request.method).toBe('GET');
    request.flush({ status: 'UP' });
  });
});
