import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { LandingComponent } from './landing.component';

describe('LandingComponent', () => {
  let fixture: ComponentFixture<LandingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LandingComponent],
      providers: [provideRouter([])],
    }).compileComponents();
    fixture = TestBed.createComponent(LandingComponent);
    fixture.detectChanges();
  });

  it('renders the primary cooking call to action', () => {
    expect(fixture.nativeElement.textContent).toContain('Start cooking');
    expect(fixture.nativeElement.querySelector('h1').textContent).toContain('Recipes with room');
  });
});
