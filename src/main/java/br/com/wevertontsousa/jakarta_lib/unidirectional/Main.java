package br.com.wevertontsousa.jakarta_lib.unidirectional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hibernate.exception.ConstraintViolationException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Persistence;
import jakarta.persistence.Table;

public class Main {

  public static void main(String[] args) {
    JpaRepository<PublisherJpaEntity> publisherRepository = new JpaRepository<>();
    var publisher = new PublisherJpaEntity("Abril");
    publisherRepository.save(publisher);

    var reviewRepository = new JpaRepository<ReviewJpaEntity>();
    var review = new ReviewJpaEntity("Muito bom");
    reviewRepository.save(review);

    var authorRepository = new JpaRepository<AuthorJpaEntity>();
    var author = new AuthorJpaEntity("Weverton");
    authorRepository.save(author);

    var bookRepository = new JpaRepository<BookJpaEntity>();
    var book = new BookJpaEntity("Aia", publisher, review, Set.of(author));
    bookRepository.save(book);

  }

}


class JpaRepository<E> {
  private static EntityManagerFactory entityManagerFactory;
  private EntityManager entityManager;
  private Class<E> class_;

  static {
    entityManagerFactory = Persistence.createEntityManagerFactory("db-postgre");
  }

  public JpaRepository() {
    this(null);
  }

  public JpaRepository(Class<E> class_) {
    this.class_ = class_;
    this.entityManager = entityManagerFactory.createEntityManager();
  }

  public void save(E model) {
    try {
      this.openTransaction().persistenceObject(model).commitTransaction();

    } catch (Exception e) {
      this.rollback();

      if (e.getCause() instanceof ConstraintViolationException) {
        throw new RuntimeException("Violação da restrição UNIQUE");
      }

      throw e;
    }
  }

  public E findById(Long id) {
    return entityManager.find(this.class_, id);
  }

  public List<E> findAll(int limit, int offset) {
    if (this.class_ == null) {
      throw new UnsupportedOperationException();
    }

    String jpql = "SELECT e FROM " + this.class_.getName() + " e";

    return entityManager
        .createQuery(jpql, this.class_)
        .setMaxResults(5)
        .setFirstResult(offset)
        .getResultList();
  }

  public void update(E model) {
    try {
      this.openTransaction().mergeObject(model).commitTransaction();

    } catch (Exception e) {
      this.rollback();

      if (e.getCause() instanceof ConstraintViolationException) {
        throw new RuntimeException("Violação da restrição UNIQUE");
      }

      throw e;
    }
  }

  public void remove(E model) {
    try {
      this.openTransaction().removeObject(model).commitTransaction();

    } catch (IllegalArgumentException e) {
      this.rollback();
      throw new RuntimeException("Recurso não encontrado");
    }
  }

  public void truncate(String sql) {
    if (this.class_ == null) {
      throw new UnsupportedOperationException();
    }

    this.openTransaction().createNativeQuery(sql).commitTransaction();
  }

  private JpaRepository<E> openTransaction() {
    this.entityManager.getTransaction().begin();
    return this;
  }

  private JpaRepository<E> commitTransaction() {
    this.entityManager.getTransaction().commit();
    return this;
  }

  private JpaRepository<E> persistenceObject(E model) {
    this.entityManager.persist(model);
    return this;
  }

  private JpaRepository<E> mergeObject(E model) {
    this.entityManager.merge(model);
    return this;
  }

  private JpaRepository<E> rollback() {
    this.entityManager.getTransaction().rollback();
    return this;
  }

  private JpaRepository<E> removeObject(E model) {
    this.entityManager.remove(model);
    return this;
  }

  private JpaRepository<E> createNativeQuery(String sql) {
    this.entityManager.createNativeQuery(sql).executeUpdate();
    return this;
  }

  public void closeEm() {
    this.entityManager.close();
  }

  public void closeEmf() {
    entityManagerFactory.close();
  }

}


@Table(name = "publishers")
@Entity
class PublisherJpaEntity {

  @GeneratedValue(strategy = GenerationType.UUID)
  @Id
  private UUID id;

  @Column(nullable = false, unique = true)
  private String name;

  public PublisherJpaEntity(String name) {
      this.name = name;
  }

}


@Table(name = "reviews")
@Entity
class ReviewJpaEntity {

  @GeneratedValue(strategy = GenerationType.UUID)
  @Id
  private UUID id;

  @Column(nullable = false)
  private String comment;

  public ReviewJpaEntity(String comment) {
      this.comment = comment;
  }

}


@Table(name = "authors")
@Entity
class AuthorJpaEntity {

  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id
  private UUID id;

  @Column(nullable = false, unique = true)
  private String name;

  public AuthorJpaEntity(String name) {
      this.name = name;
  }

}


@Table(name = "books")
@Entity
class BookJpaEntity {

  @GeneratedValue(strategy = GenerationType.UUID)
  @Id
  private UUID id;

  @Column(nullable = false, unique = true)
  private String title;

  @ManyToOne
  @JoinColumn(name = "publisher_id", nullable = false)
  private PublisherJpaEntity publisher;

  @OneToOne
  @JoinColumn(name = "review_id", nullable = false)
  private ReviewJpaEntity review;

  @ManyToMany
  @JoinTable(
      name = "books_authors",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "author_id")
  )
  private Set<AuthorJpaEntity> authors;

  public BookJpaEntity(String title, PublisherJpaEntity publisher, ReviewJpaEntity review, Set<AuthorJpaEntity> authors) {
      this.title = title;
      this.publisher = publisher;
      this.review = review;
      this.authors = authors;
  }

}
