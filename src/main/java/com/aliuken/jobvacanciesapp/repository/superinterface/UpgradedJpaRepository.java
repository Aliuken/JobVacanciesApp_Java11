package com.aliuken.jobvacanciesapp.repository.superinterface;

import com.aliuken.jobvacanciesapp.annotation.RepositoryMethod;
import com.aliuken.jobvacanciesapp.aop.aspect.ControllerAspect;
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
import java.util.Optional;

@NoRepositoryBean
public interface UpgradedJpaRepository<T extends AbstractEntity<T>> extends JpaRepository<T, Long>, UpgradedJpaRepositoryInterface<T> {
	public static final Logger log = LoggerFactory.getLogger(UpgradedJpaRepository.class);

	public static final String ENTITY_MANAGER_CACHE_NAME = "entityManagerCache";
	public static final String ENTITY_MANAGER_CACHE_ALTERNATIVE = "jpaContext";
	public static final StatisticsService ENTITY_MANAGER_CACHE_STATISTICS_SERVICE = new DefaultStatisticsService();

	@NotNull
	public static final Cache<Class<? extends AbstractEntity<?>>, EntityManager> ENTITY_MANAGER_CACHE_OBJECT = UpgradedJpaRepository.getEntityManagerCache();

	public abstract AbstractEntityFactory<T> getEntityFactory();

	public default Class<T> getEntityClass() {
		final AbstractEntityFactory<T> entityFactory = this.getEntityFactory();
		final Class<T> entityClass = entityFactory.getObjectType();

		return entityClass;
	}

	public default T getNewEntityInstance() {
		final AbstractEntityFactory<T> entityFactory = this.getEntityFactory();
		final T entityInstance = entityFactory.getObjectWithoutException();

		return entityInstance;
	}

	@RepositoryMethod
	public static <S extends AbstractEntity<S>> S getEntityStatically(final Long id, final Class<S> entityClass) {
		if(id == null) {
			return null;
		}

		final SimpleJpaRepository<S, Long> jpaRepository = UpgradedJpaRepository.getJpaRepository(entityClass);
		if(jpaRepository == null) {
			return null;
		}

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
		if(jpaRepository == null) {
			return null;
		}

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
		if(jpaRepository == null) {
			return null;
		}

		final Optional<T> optionalEntity = jpaRepository.findById(id);
		final T entity = GenericsUtils.unpackOptional(optionalEntity);
		return entity;
	}

	@Override
	@RepositoryMethod
	public default void deleteByIdAndFlush(final Long id) {
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if(jpaRepository == null) {
			return;
		}

		jpaRepository.deleteById(id);
		jpaRepository.flush();
	}

	@Override
	@RepositoryMethod
	public default <S extends T> S saveAndFlush(S entity) {
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if(jpaRepository == null) {
			return null;
		}

		entity = jpaRepository.saveAndFlush(entity);
		return entity;
	}

	@Override
	@RepositoryMethod
	public default List<T> findAll() {
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if(jpaRepository == null) {
			return null;
		}

		final List<T> result = jpaRepository.findAll();

		return result;
	}

	@Override
	@RepositoryMethod
	public default Page<T> findAll(final Pageable pageable) {
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if(jpaRepository == null) {
			return null;
		}

		final Page<T> page = jpaRepository.findAll(pageable);

		return page;
	}

	@Override
	@RepositoryMethod
	public default Page<T> findAll(final Pageable pageable, final TableField tableSortingField, final TableSortingDirection tableSortingDirection) {
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if(jpaRepository == null) {
			return null;
		}

		final Class<T> entityClass = this.getEntityClass();
		final Pageable finalPageable = this.getFinalPageable(pageable, tableSortingField, tableSortingDirection, entityClass);
		final Page<T> page = jpaRepository.findAll(finalPageable);

		return page;
	}

	@Override
	@RepositoryMethod
	public default Page<T> findAll(final Pageable pageable, final TableField tableSortingField, final TableSortingDirection tableSortingDirection, final Specification<T> specification) {
		if(specification == null) {
			throw new IllegalArgumentException("specification cannot be null");
		}

		final Class<T> entityClass = this.getEntityClass();
		final Pageable finalPageable = this.getFinalPageable(pageable, tableSortingField, tableSortingDirection, entityClass);
		final Page<T> page = this.findAll(specification, finalPageable);

		return page;
	}

	@Override
	@RepositoryMethod
	public default Page<T> findAll(final Specification<T> specification, final Pageable pageable) {
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if(jpaRepository == null) {
			return null;
		}

		final Page<T> page = jpaRepository.findAll(specification, pageable);

		return page;
	}

	@Override
	@RepositoryMethod
	public default <S extends T> List<S> findAll(final Example<S> example) {
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if(jpaRepository == null) {
			return null;
		}

		final List<S> result = jpaRepository.findAll(example);

		return result;
	}

	@Override
	@RepositoryMethod
	public default <S extends T> Page<S> findAll(final Example<S> example, final Pageable pageable) {
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if(jpaRepository == null) {
			return null;
		}

		final Page<S> page = jpaRepository.findAll(example, pageable);

		return page;
	}

	@Override
	@RepositoryMethod
	public default <S extends T> Page<S> findAll(final Example<S> example, final Pageable pageable, final TableField tableSortingField, final TableSortingDirection tableSortingDirection) {
		if(example == null) {
			throw new IllegalArgumentException("example cannot be null");
		}

		final Class<S> entityClass = example.getProbeType();
		final SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if(jpaRepository == null) {
			return null;
		}

		final Pageable finalPageable = this.getFinalPageable(pageable, tableSortingField, tableSortingDirection, entityClass);
		final Page<S> page = jpaRepository.findAll(example, finalPageable);

		return page;
	}

	private <S extends T> Pageable getFinalPageable(final Pageable pageable, final TableField tableSortingField, final TableSortingDirection tableSortingDirection, final Class<S> entityClass) {
		if(pageable == null) {
			throw new IllegalArgumentException("pageable cannot be null");
		}

		final Pageable finalPageable;
		if(tableSortingField == null) {
			finalPageable = pageable;
		} else {
			final boolean isAuthUserField = tableSortingField.isAuthUserField();
			final boolean isJobCompanyField = tableSortingField.isJobCompanyField();

			final String sortFieldPath;
			if(isAuthUserField && !AuthUser.class.equals(entityClass)) {
				sortFieldPath = tableSortingField.getFinalFieldPath();
			} else if(isJobCompanyField && !JobCompany.class.equals(entityClass)) {
				if(JobRequest.class.equals(entityClass)) {
					sortFieldPath = StringUtils.getStringJoined("jobVacancy.", tableSortingField.getFinalFieldPath());
				} else {
					sortFieldPath = tableSortingField.getFinalFieldPath();
				}
			} else {
				sortFieldPath = tableSortingField.getPartialFieldPath();
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

	private static Pageable getFinalPageable(final Pageable pageable, final String sortFieldPath, final Sort.Direction sortDirection) {
		final Sort sort = Sort.by(sortDirection, sortFieldPath);
		final Pageable finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return finalPageable;
	}

	@Override
	@RepositoryMethod
	public default T executeQuerySingleResult(final String jpqlQuery, final Map<String, Object> parameterMap) {
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
	public default List<T> executeQueryResultList(final String jpqlQuery, final Map<String, Object> parameterMap) {
		final TypedQuery<T> typedQuery = getTypedQuery(jpqlQuery, parameterMap);
		final List<T> result = typedQuery.getResultList();

		return result;
	}

	@Override
	@RepositoryMethod
	public default int executeUpdate(final String jpqlQuery, final Map<String, Object> parameterMap) {
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

	private TypedQuery<T> getTypedQuery(final String jpqlQuery, final Map<String, Object> parameterMap) {
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

	private Query getQuery(final String jpqlQuery, final Map<String, Object> parameterMap) {
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

	private SimpleJpaRepository<T, Long> getJpaRepository() {
		final Class<T> entityClass = this.getEntityClass();
		final SimpleJpaRepository<T, Long> jpaRepository = UpgradedJpaRepository.getJpaRepository(entityClass);

		return jpaRepository;
	}

	private static <S extends AbstractEntity<S>> SimpleJpaRepository<S, Long> getJpaRepository(final Class<S> entityClass) {
		final EntityManager entityManager = UpgradedJpaRepository.getEntityManagerConfigurable(entityClass);
		final SimpleJpaRepository<S, Long> jpaRepository = new SimpleJpaRepository<>(entityClass, entityManager);

		return jpaRepository;
	}

	private static <S extends AbstractEntity<S>> EntityManager getEntityManagerConfigurable(final Class<S> entityClass) {
		final boolean useEntityManagerCache = UpgradedJpaRepository.getUseEntityManagerCache();

		final EntityManager entityManager;
		if(useEntityManagerCache) {
			entityManager = UpgradedJpaRepository.getEntityManagerCacheable(entityClass);
		} else {
			entityManager = UpgradedJpaRepository.getEntityManagerNotCached(entityClass);
		}

		return entityManager;
	}

	private static <S extends AbstractEntity<S>> EntityManager getEntityManagerCacheable(final Class<S> entityClass) {
		final String entityClassName = (entityClass != null) ? entityClass.getSimpleName() : null;

		if(log.isInfoEnabled()) {
			final String traceType = ControllerAspect.getTraceType(ControllerDependentTraceType.ENTITY_MANAGER_CACHE_INPUT_TRACE);
			log.info(StringUtils.getStringJoined(traceType, "getEntityManagerCacheable. entityManager requested for entityClass ", entityClassName));
		}

		EntityManager entityManager = ENTITY_MANAGER_CACHE_OBJECT.get(entityClass);

		if(entityManager != null) {
			logGetEntityManagerCacheableResponse(ENTITY_MANAGER_CACHE_NAME, entityClassName, entityManager);
		} else {
			entityManager = UpgradedJpaRepository.getEntityManagerNotCached(entityClass);
			logGetEntityManagerCacheableResponse(ENTITY_MANAGER_CACHE_ALTERNATIVE, entityClassName, entityManager);

			if(entityManager != null) {
				ENTITY_MANAGER_CACHE_OBJECT.put(entityClass, entityManager);
			} else {
				ENTITY_MANAGER_CACHE_OBJECT.remove(entityClass);
			}
		}

		if(log.isInfoEnabled()) {
			final CacheStatistics entityManagerCacheStatistics = ENTITY_MANAGER_CACHE_STATISTICS_SERVICE.getCacheStatistics(ENTITY_MANAGER_CACHE_NAME);

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

	private static void logGetEntityManagerCacheableResponse(final String source, final String entityClassName, final EntityManager entityManager) {
		if(log.isInfoEnabled()) {
			final String traceType = ControllerAspect.getTraceType(ControllerDependentTraceType.ENTITY_MANAGER_CACHE_OUTPUT_TRACE);
			log.info(StringUtils.getStringJoined(traceType, "getEntityManagerCacheable. entityManager obtained from ", source, " for entityClass ", entityClassName, " -> ", (entityManager != null) ? "NOT_NULL" : "NULL"));
		}
	}

	private static <S extends AbstractEntity<S>> EntityManager getEntityManagerNotCached(final Class<S> entityClass) {
		final long init_time = System.currentTimeMillis();

		final JpaContext jpaContext = BeanFactoryUtils.getBean(JpaContext.class);

		EntityManager entityManager = jpaContext.getEntityManagerByManagedType(entityClass);
//		EntityManager entityManager = BeanUtils.getBean(EntityManager.class);
		if(!entityManager.isOpen()) {
			final EntityManagerFactory entityManagerFactory = BeanFactoryUtils.getBean(EntityManagerFactory.class);
			entityManager = entityManagerFactory.createEntityManager();
		}

		final long timeInside = System.currentTimeMillis() - init_time;
		final long getEntityManagerNotCachedTime = RepositoryAspectLoggingUtils.MDCgetGetEntityManagerNotCachedTime() + timeInside;
		RepositoryAspectLoggingUtils.MDCputGetEntityManagerNotCachedTime(getEntityManagerNotCachedTime);

		return entityManager;
	}

	private static Cache<Class<? extends AbstractEntity<?>>, EntityManager> getEntityManagerCache() {
		final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().using(ENTITY_MANAGER_CACHE_STATISTICS_SERVICE).build();
		cacheManager.init();

		final CacheConfiguration<Class<? extends AbstractEntity<?>>, EntityManager> cacheConfiguration = UpgradedJpaRepository.getCacheConfiguration();
		final Cache<Class<? extends AbstractEntity<?>>, EntityManager> entityManagerCache = cacheManager.createCache(ENTITY_MANAGER_CACHE_NAME, cacheConfiguration);
		return entityManagerCache;
	}

	@SuppressWarnings("rawtypes")
	private static CacheConfiguration<Class<? extends AbstractEntity<?>>, EntityManager> getCacheConfiguration() {
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
