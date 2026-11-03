import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'rm-public-layout',
  standalone: true,
  imports: [RouterLink, RouterOutlet],
  template: `
    <header class="site-header">
      <a class="brand" routerLink="/">Recipe Manager</a>
      <nav aria-label="Main navigation">
        <a routerLink="/explore">Explore</a>
        <a routerLink="/stories">Stories</a>
        <a routerLink="/pricing">Plans</a>
        <a routerLink="/auth">Sign in</a>
      </nav>
      <a class="button button-primary" routerLink="/auth">Start cooking</a>
    </header>
    <main><router-outlet /></main>
    <footer class="site-footer">
      <span>Recipe Manager</span>
      <span>Cook something worth remembering.</span>
    </footer>
  `,
})
export class PublicLayoutComponent {}
