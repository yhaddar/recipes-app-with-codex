import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'rm-landing',
  standalone: true,
  imports: [RouterLink],
  template: `
    <section class="hero shell">
      <div class="hero-copy">
        <p class="eyebrow">A calmer kitchen notebook</p>
        <h1>Recipes with room for real life.</h1>
        <p class="lede">
          Keep the recipes you love, the stories behind them, and the next meal
          in one warm, organized place.
        </p>
        <div class="hero-actions">
          <a class="button button-primary" routerLink="/auth">Start cooking</a>
          <a class="button button-secondary" routerLink="/explore">Explore recipes</a>
        </div>
      </div>
      <div class="hero-card" aria-label="Featured recipe preview">
        <span class="tag">Tonight's table</span>
        <h2>Lemon, herbs &amp; a little time</h2>
        <p>Simple recipes, saved beautifully.</p>
      </div>
    </section>
    <section class="section shell feature-grid" aria-labelledby="features-title">
      <div>
        <p class="eyebrow">Made for everyday cooking</p>
        <h2 id="features-title">From first idea to shared table.</h2>
      </div>
      <article class="panel"><span>01</span><h3>Save your way</h3><p>Build a personal collection that stays useful.</p></article>
      <article class="panel"><span>02</span><h3>Plan with ease</h3><p>Turn recipes into a week that feels possible.</p></article>
      <article class="panel"><span>03</span><h3>Share the story</h3><p>Publish recipes and notes your community can enjoy.</p></article>
    </section>
  `,
})
export class LandingComponent {}
