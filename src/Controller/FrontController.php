<?php

namespace App\Controller;
use App\Form\DoctorType;
use App\Entity\Doctor;
use App\Repository\DoctorRepository;
use App\Form\PATIENTType;
use App\Entity\PATIENT;
use App\Repository\PATIENTRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use Doctrine\ORM\EntityManagerInterface;


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
}
