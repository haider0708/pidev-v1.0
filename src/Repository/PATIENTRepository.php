<?php

namespace App\Repository;

use App\Entity\PATIENT;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Component\Security\Core\Exception\UnsupportedUserException;
use Symfony\Component\Security\Core\User\PasswordAuthenticatedUserInterface;
use Symfony\Component\Security\Core\User\PasswordUpgraderInterface;

/**
 * @extends ServiceEntityRepository<PATIENT>
* @implements PasswordUpgraderInterface<PATIENT>
 *
 * @method PATIENT|null find($id, $lockMode = null, $lockVersion = null)
 * @method PATIENT|null findOneBy(array $criteria, array $orderBy = null)
 * @method PATIENT[]    findAll()
 * @method PATIENT[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class PATIENTRepository extends ServiceEntityRepository implements PasswordUpgraderInterface
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, PATIENT::class);
    }

    /**
     * Used to upgrade (rehash) the user's password automatically over time.
     */
    public function upgradePassword(PasswordAuthenticatedUserInterface $user, string $newHashedPassword): void
    {
        if (!$user instanceof PATIENT) {
            throw new UnsupportedUserException(sprintf('Instances of "%s" are not supported.', $user::class));
        }

        $user->setPassword($newHashedPassword);
        $this->getEntityManager()->persist($user);
        $this->getEntityManager()->flush();
    }


    public function triecroissant()
    {
        return $this->createQueryBuilder('r')
            ->orderBy('r.firstname', 'ASC')

            ->getQuery()
            ->getResult();

    }
    public function triedecroissant()
    {
        return $this->createQueryBuilder('e')
            ->orderBy('e.firstname','DESC')
            ->getQuery()
            ->getResult();
    }
//    /**
//     * @return PATIENT[] Returns an array of PATIENT objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('p')
//            ->andWhere('p.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('p.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?PATIENT
//    {
//        return $this->createQueryBuilder('p')
//            ->andWhere('p.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
