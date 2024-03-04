<?php

namespace App\Repository;

use App\Entity\Livreur;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Livreur>
 *
 * @method Livreur|null find($id, $lockMode = null, $lockVersion = null)
 * @method Livreur|null findOneBy(array $criteria, array $orderBy = null)
 * @method Livreur[]    findAll()
 * @method Livreur[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class LivreurRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Livreur::class);
    }
    public function search(string $searchTerm): array
    {
        
           
          
    
            $query = $this->createQueryBuilder('l')
                ->andWhere('l.nom LIKE :searchTerm OR l.prenom LIKE :searchTerm')
                ->setParameter('searchTerm', '%'.$searchTerm.'%')
                ->orderBy('l.nom', 'ASC')
                ->getQuery();
    
            return $query->getResult();
        
           
    }
    public function triecroissant()
    {
        return $this->createQueryBuilder('r')
            ->orderBy('r.nom', 'ASC')

            ->getQuery()
            ->getResult();

    }
    public function triedecroissant()
    {
        return $this->createQueryBuilder('e')
            ->orderBy('e.nom','DESC')
            ->getQuery()
            ->getResult();
    }

//    /**
//     * @return Livreur[] Returns an array of Livreur objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('l')
//            ->andWhere('l.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('l.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?Livreur
//    {
//        return $this->createQueryBuilder('l')
//            ->andWhere('l.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
