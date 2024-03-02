<?php
namespace App\Controller;

use App\Entity\Pharmacie;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

/*class RatingController extends AbstractController
{
    #[Route('/rating', name: 'app_rating')]
    public function index(): Response
    {
        return $this->render('rating/index.html.twig', [
            'controller_name' => 'RatingController',
        ]);
    }
    
}*/
// src/Controller/RatingController.php
/*use Doctrine\ORM\EntityManagerInterface;

class RatingController extends AbstractController
{
    private $entityManager;

    public function __construct(EntityManagerInterface $entityManager)
    {
        $this->entityManager = $entityManager;
    }

    #[Route('/rate/{pharmacieId}/{note}', name: 'rate_pharmacie', methods: ['POST'])]
    public function ratePharmacie($pharmacieId, $note): JsonResponse
    {
        $pharmacie = $this->entityManager->getRepository(Pharmacie::class)->find($pharmacieId);

        if (!$pharmacie) {
            return new JsonResponse(['error' => 'Pharmacie non trouvée'], Response::HTTP_NOT_FOUND);
        }

        // Mettez à jour la note de la pharmacie
        $pharmacie->setNote($note);
        $this->entityManager->flush();

        return new JsonResponse(['success' => true]);
    }

    #[Route('/rating', name: 'app_rating')]
    public function index(): Response
    {
        $pharmacies = $this->entityManager->getRepository(Pharmacie::class)->findAll();

        return $this->render('rating/index.html.twig', [
            'controller_name' => 'RatingController',
            'pharmacies' => $pharmacies,
        ]);
    }
}
*/
// Dans votre contrôleur Symfony (par exemple, RatingController)

class RatingController extends AbstractController
{
    #[Route('/rate/{pharmacieId}/{note}', name: 'rate_pharmacie', methods: ['POST'])]
    public function ratePharmacie($pharmacieId, $note): JsonResponse
    {
        $entityManager = $this->getDoctrine()->getManager();
        $pharmacie = $entityManager->getRepository(Pharmacie::class)->find($pharmacieId);

        if (!$pharmacie) {
            return new JsonResponse(['error' => 'Pharmacie non trouvée'], Response::HTTP_NOT_FOUND);
        }

        // Mettez à jour la note de la pharmacie
        $pharmacie->setNote($note);
        $entityManager->flush();

        return new JsonResponse(['success' => true]);
    }
}
