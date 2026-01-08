package com.aliuken.jobvacanciesapp.repository.superinterface;

import com.aliuken.jobvacanciesapp.annotation.RepositoryMethod;
import com.aliuken.jobvacanciesapp.aop.aspect.ControllerAspect;
import com.aliuken.jobvacanciesapp.config.CacheConfig;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.ControllerDependentTraceType;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableField;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.spring.aop.logging.RepositoryAspectLoggingUtils;
import com.aliuken.jobvacanciesapp.util.spring.di.BeanFactoryUtils;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.core.internal.statistics.DefaultStatisticsService;
import org.ehcache.core.spi.service.StatisticsService;
import org.ehcache.core.statistics.CacheStatistics;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@NoRepositoryBean
public interface UpgradedJpaRepository<T extends AbstractEntity<T>> extends JpaRepository<T, Long>, UpgradedJpaRepositoryInterface<T> {
	public static final @NonNull Logger log = LoggerFactory.getLogger(UpgradedJpaRepository.class);
	public static final @NonNull StatisticsService ENTITY_MANAGER_CACHE_STATISTICS_SERVICE = new DefaultStatisticsService();
	public static final @NonNull Cache<Class<? extends AbstractEntity<?>>, EntityManager> ENTITY_MANAGER_CACHE_OBJECT = UpgradedJpaRepository.getEntityManagerCache();

	public abstract @NonNull AbstractEntityFactory<T> getEntityFactory();

	public default @NonNull Class<T> getEntityClass() {
		final AbstractEntityFactory<T> entityFactory = this.getEntityFactory();
		final Class<T> entityClass = entityFactory.getObjectType();

		return entityClass;
	}

	public default @NotNull T getNewEntityInstance() {
		final AbstractEntityFactory<T> entityFactory = this.getEntityFactory();
		final T entityInstance = entityFactory.getObjectWithoutException();
		return entityInstance;
	}

	@RepositoryMethod
	public static <S extends AbstractEntity<S>> S getEntityStatically(final Long id, final @NonNull Class<S> entityClass) {
		if(id == null) {
			return null;
		}

		final SimpleJpaRepository<S, Long> jpaRepository = UpgradedJpaRepository.getJpaRepository(entityClass);
		final Optional<S> optionalEntity = jpaRepository.findById(id);
		final S entity = GenericsUtils.unpackOptional(optionalEntity);
		return entity;
	}

	@Override
	@RepositoryMethod
	public default T findByIdNotOptional(final Long id) {
		if(id == null) {
			return null;
		}

		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		final Optional<T> optionalEntity = jpaRepository.findById(id);
		final T entity = GenericsUtils.unpackOptional(optionalEntity);
		return entity;

//		Alternativa comentada porque repetiría el @RepositoryMethod en findByIdNotOptional y getEntityStatically
//		final Class<T> entityClass = this.getEntityClass();
//		final T entity = UpgradedJpaRepository.getEntityStatically(id, entityClass);
//		return entity;
	}

	@Override
	@RepositoryMethod
	public default T findByIdOrNewEntity(final Long id) {
		if(id == null) {
			return this.getNewEntityInstance();
		}

		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		final Optional<T> optionalEntity = jpaRepository.findById(id);
		final T entity = GenericsUtils.unpackOptional(optionalEntity);
		return entity;
	}

	@Override
	@RepositoryMethod
	public default void deleteByIdAndFlush(final Long id) {
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		jpaRepository.deleteById(id);
		jpaRepository.flush();
	}

	@Override
	@RepositoryMethod
	public default <S extends T> @NonNull S saveAndFlush(@NonNull S entity) {
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		entity = jpaRepository.saveAndFlush(entity);
		return entity;
	}

	@Override
	@RepositoryMethod
	public default @NonNull List<T> findAll() {
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		final List<T> result = jpaRepository.findAll();
		return result;
	}

	@Override
	@RepositoryMethod
	public default @NonNull Page<T> findAll(final @NonNull Pageable pageable) {
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		final Page<T> page = jpaRepository.findAll(pageable);
		return page;
	}

	@Override
	@RepositoryMethod
	public default @NonNull Page<T> findAll(final @NonNull Pageable pageable, final TableField sortingTableField, final TableSortingDirection tableSortingDirection) {
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		final Class<T> entityClass = this.getEntityClass();
		final Pageable finalPageable = this.getFinalPageable(pageable, sortingTableField, tableSortingDirection, entityClass);
		final Page<T> page = jpaRepository.findAll(finalPageable);
		return page;
	}

	@Override
	@RepositoryMethod
	public default @NonNull Page<T> findAll(final @NonNull Pageable pageable, final TableField sortingTableField, final TableSortingDirection tableSortingDirection, final Specification<T> specification) {
		if(specification == null) {
			throw new IllegalArgumentException("specification cannot be null");
		}

		final Class<T> entityClass = this.getEntityClass();
		final Pageable finalPageable = this.getFinalPageable(pageable, sortingTableField, tableSortingDirection, entityClass);
		final Page<T> page = this.findAll(specification, finalPageable);
		return page;
	}

	@Override
	@RepositoryMethod
	public default @NonNull Page<T> findAll(final Specification<T> specification, final @NonNull Pageable pageable) {
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		final Page<T> page = jpaRepository.findAll(specification, pageable);
		return page;
	}

	@Override
	@RepositoryMethod
	public default <S extends T> @NonNull List<S> findAll(final @NonNull Example<S> example) {
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		final List<S> result = jpaRepository.findAll(example);
		return result;
	}

	@Override
	@RepositoryMethod
	public default <S extends T> @NonNull Page<S> findAll(final @NonNull Example<S> example, final @NonNull Pageable pageable) {
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		final Page<S> page = jpaRepository.findAll(example, pageable);
		return page;
	}

	@Override
	@RepositoryMethod
	public default <S extends T> @NonNull Page<S> findAll(final @NonNull Example<S> example, final @NonNull Pageable pageable, final TableField sortingTableField, final TableSortingDirection tableSortingDirection) {
		final Class<S> entityClass = example.getProbeType();
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		final Pageable finalPageable = this.getFinalPageable(pageable, sortingTableField, tableSortingDirection, entityClass);
		final Page<S> page = jpaRepository.findAll(example, finalPageable);
		return page;
	}

	private <S extends T> @NonNull Pageable getFinalPageable(final @NonNull Pageable pageable, final TableField sortingTableField, final TableSortingDirection tableSortingDirection, final Class<S> entityClass) {
		final Pageable finalPageable;
		if(sortingTableField == null) {
			finalPageable = pageable;
		} else {
			final boolean isAuthUserField = sortingTableField.isAuthUserField();
			final boolean isJobCompanyField = sortingTableField.isJobCompanyField();

			final String sortFieldPath;
			if(isAuthUserField && !AuthUser.class.equals(entityClass)) {
				sortFieldPath = sortingTableField.getFinalFieldPath();
			} else if(isJobCompanyField && !JobCompany.class.equals(entityClass)) {
				if(JobRequest.class.equals(entityClass)) {
					sortFieldPath = StringUtils.getStringJoined("jobVacancy.", sortingTableField.getFinalFieldPath());
				} else {
					sortFieldPath = sortingTableField.getFinalFieldPath();
				}
			} else {
				sortFieldPath = sortingTableField.getPartialFieldPath();
			}

			final Sort.Direction sortDirection;
			if(tableSortingDirection != null) {
				sortDirection = tableSortingDirection.getSortDirection();
			} else {
				sortDirection = Sort.Direction.ASC;
			}

			finalPageable = UpgradedJpaRepository.getFinalPageable(pageable, sortFieldPath, sortDirection);
		}

		return finalPageable;
	}

	private static @NonNull Pageable getFinalPageable(final @NonNull Pageable pageable, final @NonNull String sortFieldPath, final Sort.Direction sortDirection) {
		final Sort sort = Sort.by(sortDirection, sortFieldPath);
		final Pageable finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return finalPageable;
	}

	@Override
	@RepositoryMethod
	public default T executeQuerySingleResult(final @NonNull String jpqlQuery, final Map<String, Object> parameterMap) {
		final TypedQuery<T> typedQuery = getTypedQuery(jpqlQuery, parameterMap);
		try {
			final T result = typedQuery.getSingleResult();
			return result;
		} catch(final NoResultException exception) {
			return null;
		}
	}

	@Override
	@RepositoryMethod
	public default @NonNull List<T> executeQueryResultList(final @NonNull String jpqlQuery, final Map<String, Object> parameterMap) {
		final TypedQuery<T> typedQuery = getTypedQuery(jpqlQuery, parameterMap);
		final List<T> result = typedQuery.getResultList();
		if(result != null) {
			return result;
		} else {
			return List.of();
		}
	}

	@Override
	@RepositoryMethod
	public default int executeUpdate(final @NonNull String jpqlQuery, final Map<String, Object> parameterMap) {
		final Query query = getQuery(jpqlQuery, parameterMap);
		final int rowsUpdated = query.executeUpdate();
		return rowsUpdated;
	}

	@Override
	@RepositoryMethod
	public default T refreshEntity(T entity) {
		if(entity == null) {
			return null;
		}

		final Long entityId = entity.getId();
		if(entityId == null) {
			return null;
		}

		final Class<?> initialEntityClass = entity.getClass();
		final Class<T> entityClass = GenericsUtils.cast(initialEntityClass);
		final EntityManager entityManager = UpgradedJpaRepository.getEntityManagerConfigurable(entityClass);
		entity = entityManager.find(entityClass, entityId);
		return entity;
	}

	private @NonNull TypedQuery<T> getTypedQuery(final @NonNull String jpqlQuery, final Map<String, Object> parameterMap) {
		final Class<T> entityClass = this.getEntityClass();
		final EntityManager entityManager = UpgradedJpaRepository.getEntityManagerConfigurable(entityClass);

		final TypedQuery<T> typedQuery = entityManager.createQuery(jpqlQuery, entityClass);
		if(parameterMap != null) {
			for(final Map.Entry<String, Object> parameterMapEntry : parameterMap.entrySet()) {
				typedQuery.setParameter(parameterMapEntry.getKey(), parameterMapEntry.getValue());
			}
		}
		return typedQuery;
	}

	private @NonNull Query getQuery(final @NonNull String jpqlQuery, final Map<String, Object> parameterMap) {
		final Class<T> entityClass = this.getEntityClass();
		final EntityManager entityManager = UpgradedJpaRepository.getEntityManagerConfigurable(entityClass);

		final Query query = entityManager.createQuery(jpqlQuery);
		if(parameterMap != null) {
			for(final Map.Entry<String, Object> parameterMapEntry : parameterMap.entrySet()) {
				query.setParameter(parameterMapEntry.getKey(), parameterMapEntry.getValue());
			}
		}
		return query;
	}

	private @NonNull SimpleJpaRepository<T, Long> getJpaRepository() {
		final Class<T> entityClass = this.getEntityClass();
		final SimpleJpaRepository<T, Long> jpaRepository = UpgradedJpaRepository.getJpaRepository(entityClass);
		return jpaRepository;
	}

	private static <S extends AbstractEntity<S>> @NonNull SimpleJpaRepository<S, Long> getJpaRepository(final @NonNull Class<S> entityClass) {
		final EntityManager entityManager = UpgradedJpaRepository.getEntityManagerConfigurable(entityClass);
		final SimpleJpaRepository<S, Long> jpaRepository = new SimpleJpaRepository<>(entityClass, entityManager);
		return jpaRepository;
	}

	private static <S extends AbstractEntity<S>> @NonNull EntityManager getEntityManagerConfigurable(@NonNull final Class<S> entityClass) {
		final boolean useEntityManagerCache = UpgradedJpaRepository.getUseEntityManagerCache();

		final EntityManager entityManager;
		if(useEntityManagerCache) {
			entityManager = UpgradedJpaRepository.getEntityManagerCacheable(entityClass);
		} else {
			entityManager = UpgradedJpaRepository.getEntityManagerNotCached(entityClass);
		}
		return entityManager;
	}

	private static <S extends AbstractEntity<S>> @NonNull EntityManager getEntityManagerCacheable(@NonNull final Class<S> entityClass) {
		final String entityClassName = entityClass.getSimpleName();

		if(log.isInfoEnabled()) {
			final String traceType = ControllerAspect.getTraceType(ControllerDependentTraceType.ENTITY_MANAGER_CACHE_INPUT_TRACE);
			log.info(StringUtils.getStringJoined(traceType, "getEntityManagerCacheable. entityManager requested for entityClass ", entityClassName));
		}

		EntityManager entityManager = ENTITY_MANAGER_CACHE_OBJECT.get(entityClass);

		if(entityManager != null) {
			logGetEntityManagerCacheableResponse(CacheConfig.ENTITY_MANAGER_CACHE_NAME, entityClassName, entityManager);
		} else {
			entityManager = UpgradedJpaRepository.getEntityManagerNotCached(entityClass);
			logGetEntityManagerCacheableResponse(CacheConfig.ENTITY_MANAGER_CACHE_ALTERNATIVE, entityClassName, entityManager);
			ENTITY_MANAGER_CACHE_OBJECT.put(entityClass, entityManager);

//          //Back when getEntityManagerNotCached wasn't yet @NonNull
//			if(entityManager != null) {
//				ENTITY_MANAGER_CACHE_OBJECT.put(entityClass, entityManager);
//			} else {
//				ENTITY_MANAGER_CACHE_OBJECT.remove(entityClass);
//			}
		}

		if(log.isInfoEnabled()) {
			final CacheStatistics entityManagerCacheStatistics = ENTITY_MANAGER_CACHE_STATISTICS_SERVICE.getCacheStatistics(CacheConfig.ENTITY_MANAGER_CACHE_NAME);

			final long hits = entityManagerCacheStatistics.getCacheHits();
			final long misses = entityManagerCacheStatistics.getCacheMisses();
			final long size = entityManagerCacheStatistics.getTierStatistics().get("OnHeap").getMappings();
			final long bytes = entityManagerCacheStatistics.getTierStatistics().get("OnHeap").getOccupiedByteSize();
			final String formattedStatistics = String.format("hits/misses: %d/%d, items: %d, size (bytes): %d", hits, misses, size, bytes);

			final String traceType = ControllerAspect.getTraceType(ControllerDependentTraceType.ENTITY_MANAGER_CACHE_SUMMARY_TRACE);
			log.info(StringUtils.getStringJoined(traceType, "getEntityManagerCacheable. cache statistics: ", formattedStatistics));
		}

		return entityManager;
	}

	private static void logGetEntityManagerCacheableResponse(final @NonNull String source, final @NonNull String entityClassName, final @NonNull EntityManager entityManager) {
		if(log.isInfoEnabled()) {
			final String traceType = ControllerAspect.getTraceType(ControllerDependentTraceType.ENTITY_MANAGER_CACHE_OUTPUT_TRACE);
			log.info(StringUtils.getStringJoined(traceType, "getEntityManagerCacheable. entityManager obtained from ", source, " for entityClass ", entityClassName, " -> NOT_NULL"));
//          //Back when entityManager wasn't yet @NonNull
//			log.info(StringUtils.getStringJoined(traceType, "getEntityManagerCacheable. entityManager obtained from ", source, " for entityClass ", entityClassName, " -> ", (entityManager != null) ? "NOT_NULL" : "NULL"));
		}
	}

	private static <S extends AbstractEntity<S>> @NonNull EntityManager getEntityManagerNotCached(final @NonNull Class<S> entityClass) {
		final long init_time = System.currentTimeMillis();

		final JpaContext jpaContext = BeanFactoryUtils.getBean(JpaContext.class);

		EntityManager entityManager = jpaContext.getEntityManagerByManagedType(entityClass);
//		EntityManager entityManager = BeanFactoryUtils.getBean(EntityManager.class);
		if(!entityManager.isOpen()) {
			final EntityManagerFactory entityManagerFactory = BeanFactoryUtils.getBean(EntityManagerFactory.class);
			entityManager = entityManagerFactory.createEntityManager();
			Objects.requireNonNull(entityManager, "entityManager cannot be null");
		}

		final long timeInside = System.currentTimeMillis() - init_time;
		final long getEntityManagerNotCachedTime = RepositoryAspectLoggingUtils.MDCgetGetEntityManagerNotCachedTime() + timeInside;
		RepositoryAspectLoggingUtils.MDCputGetEntityManagerNotCachedTime(getEntityManagerNotCachedTime);

		return entityManager;
	}

	private static @NonNull Cache<Class<? extends AbstractEntity<?>>, EntityManager> getEntityManagerCache() {
        final Cache<Class<? extends AbstractEntity<?>>, EntityManager> entityManagerCache;
        try (CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().using(ENTITY_MANAGER_CACHE_STATISTICS_SERVICE).build()) {
            cacheManager.init();

            final CacheConfiguration<Class<? extends AbstractEntity<?>>, EntityManager> cacheConfiguration = UpgradedJpaRepository.getCacheConfiguration();
            entityManagerCache = cacheManager.createCache(CacheConfig.ENTITY_MANAGER_CACHE_NAME, cacheConfiguration);
        }
		Objects.requireNonNull(entityManagerCache, "entityManagerCache cannot be null");
        return entityManagerCache;
	}

	@SuppressWarnings("rawtypes")
	private static @NonNull CacheConfiguration<Class<? extends AbstractEntity<?>>, EntityManager> getCacheConfiguration() {
		final ResourcePools resourcePools = ResourcePoolsBuilder.heap(10).build();
		final CacheConfiguration<Class, EntityManager> cacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Class.class, EntityManager.class, resourcePools).build();
		final CacheConfiguration<Class<? extends AbstractEntity<?>>, EntityManager> cacheConfigurationWithGenerics = GenericsUtils.cast(cacheConfiguration);
		return cacheConfigurationWithGenerics;
	}

	private static boolean getUseEntityManagerCache() {
		final ConfigPropertiesBean configPropertiesBean = BeanFactoryUtils.getBean(ConfigPropertiesBean.class);
		final boolean useEntityManagerCache = configPropertiesBean.isUseEntityManagerCache();
		return useEntityManagerCache;
	}
}
