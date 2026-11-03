package com.recipemanager.email;

public record EmailMessage(String recipient, String subject, String text) {
}
