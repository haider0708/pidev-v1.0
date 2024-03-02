<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Http\Authentication\AuthenticationUtils;

class AdminController extends AbstractController
{
    #[Route(path: '/admin_login', name: 'app_admin_login')]
    public function login(AuthenticationUtils $authenticationUtils): Response
    {
         // Check if the user is authenticated
        if ($this->isGranted('IS_AUTHENTICATED_FULLY')) 
        {
            // Check if the user has the required role
            if (!$this->isGranted('ROLE_ADMIN') || !$this->isGranted('ROLE_SUPER_ADMIN'))
             {
                // Deny access
                throw $this->createAccessDeniedException();
             }
        }
        // if ($this->getUser()) {
        //     return $this->redirectToRoute('target_path');
        // }

        // get the login error if there is one
        $error = $authenticationUtils->getLastAuthenticationError();
        // last username entered by the user
        $lastUsername = $authenticationUtils->getLastUsername();
        
        return $this->render('security/login.html.twig', ['last_username' => $lastUsername, 'error' => $error]);
    }

    #[Route(path: '/logoutA', name: 'app_logout_admin')]
    public function logout(): void
    {
        throw new \LogicException('This method can be blank - it will be intercepted by the logout key on your firewall.');
    }
}
