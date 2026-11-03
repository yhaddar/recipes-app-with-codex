CREATE TABLE subscription_plans (
    id UUID NOT NULL,
    code VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    price_amount NUMERIC(12, 2) NOT NULL DEFAULT 0,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_subscription_plans PRIMARY KEY (id),
    CONSTRAINT uk_subscription_plans_code UNIQUE (code),
    CONSTRAINT ck_subscription_plans_code CHECK (code IN ('FREE_TRIAL', 'FREE', 'PRO', 'PREMIUM')),
    CONSTRAINT ck_subscription_plans_price CHECK (price_amount >= 0)
);

CREATE TABLE subscriptions (
    id UUID NOT NULL,
    user_id UUID NOT NULL,
    plan_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    starts_at TIMESTAMPTZ NOT NULL,
    ends_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_subscriptions PRIMARY KEY (id),
    CONSTRAINT fk_subscriptions_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_subscriptions_plan FOREIGN KEY (plan_id) REFERENCES subscription_plans (id) ON DELETE RESTRICT,
    CONSTRAINT ck_subscriptions_status CHECK (status IN ('TRIAL', 'ACTIVE', 'CANCELLED', 'EXPIRED')),
    CONSTRAINT ck_subscriptions_period CHECK (ends_at IS NULL OR ends_at > starts_at)
);
