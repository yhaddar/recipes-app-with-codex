# Recipe Manager — Version 1 Design

## Design philosophy

Recipe Manager is designed as a warm digital kitchen notebook: structured enough to reduce friction, expressive enough to keep cooking personal. The interface uses editorial typography, generous whitespace, tactile borders, and a food-led palette instead of a generic SaaS dashboard. Public pages feel open and exploratory; private workspaces become calmer and more task-oriented; Admin pages are denser but retain the same hospitality.

## Visual identity

- **Palette:** paper `#fbf8f2`, linen `#f1eadf`, ink `#20251f`, leaf `#416348`, deep leaf `#293f31`, terracotta `#b94e35`, and harvest gold `#e3ad4f`.
- **Typography:** Georgia is used for expressive editorial headings; the system sans-serif stack handles UI labels, navigation, forms, tables, and metadata.
- **Shape language:** 8–18px control and card radii, occasional large image crops, fine warm-gray borders, and restrained shadows. No glassmorphism or decorative gradients are used.
- **Spacing:** the layout follows a small rhythm of 8px increments, with 14–28px component padding and 56–90px section spacing.

## Version 1 technology

The prototype intentionally remains HTML5 and CSS3 only. It does not introduce Angular, Tailwind CSS, PrimeNG, GSAP, Motion, package configuration, or a build step. Native links, forms, anchors, checkboxes, radio inputs, and `<details>` provide the available interactions.

## Design tokens

All global tokens live in `prototype/styles.css` under `:root`: colors, type stacks, radius, and shadow. Reusable classes include `.button`, `.eyebrow`, `.tag`, `.status`, `.panel`, `.recipe-card`, `.field`, `.section`, and `.shell`.

## Navigation and page inventory

The public header links to Explore, Stories, Plans, About, Sign in, and Start cooking. The user workspace uses a focused side navigation for Overview, My recipes, New recipe, Tasks, Stories, Meal planner, Favorites, Profile, and Subscription. The Admin workspace has its own role-specific navigation for Users, Moderation, Taxonomy, Reports, Plans, and Activity.

Implemented pages:

- `prototype/index.html` — landing page and discovery entry point.
- `prototype/explore.html` — searchable/filterable recipe exploration surface.
- `prototype/recipe.html` — recipe detail with ingredients, instructions, save action, and community note.
- `prototype/blog.html` / `prototype/article.html` — story listing and story detail.
- `prototype/creator.html` — creator profile and recipe collection.
- `prototype/about.html`, `prototype/pricing.html`, `prototype/privacy.html`, `prototype/terms.html` — platform and legal pages.
- `prototype/auth.html` — shared Login/Register interface with CSS-only switching, reset affordance, and icon-only Google/Facebook controls.
- `prototype/dashboard.html` — user dashboard with metrics, recipes, tasks, planner, stories, profile, and subscription panels.
- `prototype/create-recipe.html` — recipe creation form with image, ingredients, instructions, draft/publish states.
- `prototype/create-blog.html` — story editor with cover image, categories, tags, draft/publish states.
- `prototype/admin.html` — Admin dashboard with users, moderation, taxonomy, reports, plans, and activity.
- `prototype/README.md` — local run instructions and Version 1 limitations.

All prototype HTML, CSS, assets, and local instructions are contained in `prototype/`.

## Responsive behavior

The layout uses CSS Grid and Flexbox with two breakpoints. At tablet widths, public grids reduce to two columns and the user sidebar becomes a compact rail. At mobile widths, grids become single-column, the sidebar becomes a horizontal scrollable navigation, forms stack, tables scroll safely, and hero artwork and typography reduce without hiding primary actions.

## Authentication interaction

`prototype/auth.html` keeps Login and Register in one interface. A native radio-input switch makes the active mode clear and reveals the relevant form without JavaScript. Google and Facebook are represented by icon-only circular controls with accessible names and tooltips; provider names are not shown as visible labels. The login mode includes a native password-reset disclosure.

## Interaction patterns

The prototype uses native links, anchors, forms, `required` fields, select controls, checkboxes, and CSS focus/hover/active states. The main journeys are connected as follows:

1. Landing → Explore → Recipe detail → Creator profile.
2. Sign in → Dashboard → New recipe → Preview/publish.
3. Dashboard → Tasks / Planner.
4. Dashboard → New story → Story preview.
5. Explore → Recipe → Save / community note.
6. Admin login concept → Admin dashboard → Users / Moderation / Reports / Plans.

## Accessibility decisions

Pages use a declared English document language, semantic headings, landmarks, labeled inputs, required states, descriptive image alt text, native controls, visible keyboard focus styles, readable contrast, and responsive touch targets. The prototype does not claim full automated accessibility conformance.

## Visual references

The local SVG illustrations in `prototype/assets/` are the prototype’s visual reference set: `hero-table.svg`, `pasta.svg`, `salad.svg`, and `bread.svg`. The landing page, Explore page, dashboard, recipe editor, and authentication interface were reviewed in Safari at desktop width during verification.

## Known limitations

This is intentionally a static HTML/CSS Version 1 prototype. It has no JavaScript, backend, Angular application, real authentication, persistence, database, image upload, email delivery, search execution, or real form submission. Buttons and forms demonstrate intended destinations and states; they do not process real data. The food visuals are local SVG illustrations created for this prototype, so production photography and final content remain future design inputs.
