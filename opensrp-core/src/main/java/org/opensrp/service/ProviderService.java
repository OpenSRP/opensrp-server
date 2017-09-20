package org.opensrp.service;

import java.util.List;

import org.opensrp.domain.Provider;
import org.opensrp.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderService {

	private final AllProviders allProviders;

	@Autowired
	public ProviderService(AllProviders allProviders) {
		this.allProviders = allProviders;
	}

	public Provider getProviderByBaseEntityId(String baseEntityId) {
		return allProviders.findByBaseEntityId(baseEntityId);
	}

	public List<Provider> getAllProviders() {
		return allProviders.findAllProviders();
	}

	public void addProvider(Provider provider) {
		allProviders.add(provider);
	}

	public void updateProvider(Provider provider) {
		allProviders.update(provider);
	}
}
