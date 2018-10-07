package me.piggypiglet.gary.core.objects.paginations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PaginationSet {
    private List<PaginationPage> pages;

    public PaginationSet() {
        pages = new ArrayList<>();
    }

    /**
     * Add a list of pages to a set.
     * @param pages The pages to be added.
     * @return Returns an instance of PaginationSet that includes the referenced pages.
     */
    public PaginationSet addPages(List<PaginationPage> pages) {
        this.pages = pages;

        return this;
    }

    /**
     * Get the individual PaginationPage that belongs to an emote.
     * @param emote The emote that the PaginationPage belongs to.
     * @return Returns the PaginationPage belonging to the referenced emote.
     */
    public PaginationPage getPage(Object emote) {
        AtomicReference<PaginationPage> page = new AtomicReference<>();

        pages.forEach(page_ -> {
            if (page_.getEmote().equals(emote)) {
                page.set(page_);
            }
        });

        return page.get();
    }
}