package br.com.wevertontsousa.jakarta_lib.bidirectional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hibernate.exception.ConstraintViolationException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Persistence;
import jakarta.persistence.Table;

public class Main {

  public static void main(String[] args) {
    PublisherJpaEntity publisher = new PublisherJpaEntity("Abril");
    ReviewJpaEntity review = new ReviewJpaEntity("Muito bom");
    AuthorJpaEntity author = new AuthorJpaEntity("Weverton");
    BookJpaEntity book = new BookJpaEntity("Aia", publisher, review, Set.of(author));

    /*
      * Como em todos os relacionamentos definidos em BookJpaEntity contém
      * `cascade = CascadeType.` as demais entidades vão ser persistidas
      * através do repositório `JpaRepository<BookJpaEntity>()` implicitamente.
      */
    JpaRepository<BookJpaEntity> bookRepository = new JpaRepository<>();
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


@Table(schema = "bidirectional", name = "publishers")
@Entity(name = "PublisherJpaEntityBidirectional")
class PublisherJpaEntity {

  @GeneratedValue(strategy = GenerationType.UUID)
  @Id
  private UUID id;

  @Column(nullable = false, unique = true)
  private String name;

  @OneToMany(mappedBy = "publisher", fetch = FetchType.LAZY)
  private Set<BookJpaEntity> books;

  public PublisherJpaEntity(String name) {
    this.name = name;
  }

}


@Table(schema = "bidirectional", name = "authors")
@Entity(name = "AuthorJpaEntityBidirectional")
class AuthorJpaEntity {

  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id
  private UUID id;

  @Column(nullable = false, unique = true)
  private String name;

  @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
  private Set<BookJpaEntity> books;

  public AuthorJpaEntity(String name) {
    this.name = name;
  }

}


@Table(schema = "bidirectional", name = "reviews")
@Entity(name = "ReviewJpaEntityBidirectional")
class ReviewJpaEntity {

  @GeneratedValue(strategy = GenerationType.UUID)
  @Id
  private UUID id;

  @Column(nullable = false)
  private String comment;

  @OneToOne(mappedBy = "review")
  private BookJpaEntity book;

  public ReviewJpaEntity(String comment) {
    this.comment = comment;
  }

}


@Table(schema = "bidirectional", name = "books")
@Entity(name = "BookJpaEntityBidirectional")
class BookJpaEntity {

  @GeneratedValue(strategy = GenerationType.UUID)
  @Id
  private UUID id;

  @Column(nullable = false, unique = true)
  private String title;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "publisher_id", nullable = false)
  private PublisherJpaEntity publisher;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "review_id", nullable = false)
  private ReviewJpaEntity review;

  @ManyToMany(cascade = CascadeType.ALL)
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
