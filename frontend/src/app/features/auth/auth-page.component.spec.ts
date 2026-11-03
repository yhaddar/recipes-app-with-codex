import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { AuthPageComponent } from './auth-page.component';

describe('AuthPageComponent', () => {
  let fixture: ComponentFixture<AuthPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AuthPageComponent],
      providers: [provideRouter([])],
    }).compileComponents();
    fixture = TestBed.createComponent(AuthPageComponent);
    fixture.detectChanges();
  });

  it('keeps submission disabled until required login fields are valid', () => {
    expect(fixture.nativeElement.querySelector('button.submit').disabled).toBeTrue();
  });

  it('switches between login and registration fields', () => {
    (fixture.nativeElement.querySelectorAll('.auth-switch button')[1] as HTMLButtonElement).click();
    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('First name');
    expect(fixture.nativeElement.textContent).toContain('Create account');
  });

  it('requires names in registration mode', () => {
    (fixture.nativeElement.querySelectorAll('.auth-switch button')[1] as HTMLButtonElement).click();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('button.submit').disabled).toBeTrue();
  });
});
