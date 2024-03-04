<?php

namespace App\Controller;
use App\Form\DoctorType;
use App\Entity\Doctor;
use App\Repository\DoctorRepository;
use App\Repository\EventRepository;
use App\Form\PATIENTType;
use App\Entity\PATIENT;
use App\Repository\PharmacieRepository;
use App\Repository\OrdonnanceRepository;
use App\Repository\PATIENTRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use Doctrine\ORM\EntityManagerInterface;
use App\Repository\ProduitRepository;
use Knp\Component\Pager\PaginatorInterface;



class FrontController extends AbstractController
{
    #[Route('/front', name: 'app_front')]
    public function index(): Response
    {
        return $this->render('front/index.html.twig', [
            'controller_name' => 'FrontController',
        ]);
    }

    #[Route('/front_doctor', name: 'app_front_doctor')]


    public function Affiche_Doctor (DoctorRepository $repository)
        {
            $doctor=$repository->findAll() ; //select *
            return $this->render('front/affiche_doctor.html.twig',['doctor'=>$doctor]);
        }
    
        //gestion Malek
        #[Route('/event_front', name: 'event_front')]
        public function frontAffiche(EventRepository $eventRepository): Response
        {
            
            $events = $eventRepository->findAll();
        
           
            return $this->render('event/indexFront.html.twig', [
                'events' => $eventRepository->findAll(),
            ]);
        
        
        }
        //fin gestion malek
//Gestion manel
        #[Route('/front_patient/{id}', name: 'app_front_patient')]
        public function edit(PatientRepository $repository, $id, Request $request,UserPasswordHasherInterface $userPasswordHasher)
        {
        $patient = $repository->find($id);
        $form = $this->createForm(PATIENTType::class, $patient);

        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            // encode the plain password
            $patient->setPassword(
                $userPasswordHasher->hashPassword(
                $patient,
                $form->get('password')->getData()
                )
            );
            
            $uploadedFile = $form['img_path']->getData();
            $newFilename = md5(uniqid()) . '.' . $uploadedFile->guessExtension();
            $uploadDirectory = $this->getParameter('kernel.project_dir') . '/public/uploads';
            $uploadedFile->move($uploadDirectory, $newFilename);
            $patient->setimg_path('uploads/' . $newFilename);
            $em = $this->getDoctrine()->getManager();
            $em->flush(); // Correction : Utilisez la méthode flush() sur l'EntityManager pour enregistrer les modifications en base de données.
            return $this->redirectToRoute("app_front");
        }

        return $this->render('front/affiche_patient.html.twig', [
            'form' => $form->createView(),
        ]);
    }
    #[Route('/Affichefr', name: 'app_Affichefr')]
    public function Affiche (PharmacieRepository $repository)
        {
          $pharmacie=$repository->findAll() ; //select *
            //return $this->render('GestionPharmacie/Affiche.html.twig',['pharmacie'=>$pharmacie]);
            return $this->render('front/affichefront.html.twig',['pharmacie'=>$pharmacie]);
        }
    #[Route('/Afficheor', name: 'app_Afficheor')]
    public function Afficheordonnance (OrdonnanceRepository $repository)
        {
        $ordonnance=$repository->findAll() ; //select *
            //return $this->render('GestionPharmacie/Affiche.html.twig',['pharmacie'=>$pharmacie]);
           return $this->render('front/affichefrontord.html.twig',['ordonnance'=>$ordonnance]);
        }
        //fin Gestion manel


        /// Gestion ahmed
        #[Route('/Affichef', name: 'app_affichef')]
    public function affiche1(ProduitRepository $repository,PaginatorInterface $paginator,Request $request): Response
    {
        $produit = $paginator->paginate(
            $repository->findAll(),
            $request->query->getInt('page', 1),
            6
        );
        //return $this->render('Produit/Affichef.html.twig', ['produit' => $produit]);
         return $this->render('front/affichef.html.twig', ['produit' => $produit]);
    }
    private $entityManager;

    public function __construct(EntityManagerInterface $entityManager)
    {
        $this->entityManager = $entityManager;
    }
    
    public function affichef(ProduitRepository $repository)
    {
        $Produit = $repository->findAll(); // Récupérer les Produit depuis le repository
        return $this->render('front/affichef.html.twig', ['produit' => $Produit]); // Passer les Produit au template Twig
    }
}
