package com.hmellema.anvil.plugin;

import com.hmellema.anvil.plugin.resolvers.AnvilOperationResolver;
import com.hmellema.sgf4j.extension.ResolverExtension;
import com.hmellema.sgf4j.resolving.Resolver;
import org.pf4j.Extension;

import java.util.Collection;
import java.util.List;

@Extension
public class AnvilResolverExtension implements ResolverExtension {
    private static final String EXTENSION_NAME = "anvil";

    private static final List<Resolver> RESOLVER_LIST = List.of(
            new AnvilOperationResolver()
    );

    @Override
    public String getName() {
        return EXTENSION_NAME;
    }

    @Override
    public Collection<Resolver> getResolvers() {
        return RESOLVER_LIST;
    }
}
