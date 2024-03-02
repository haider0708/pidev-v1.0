<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20240302120809 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE ordonnance (id INT AUTO_INCREMENT NOT NULL, pharmacie_id INT DEFAULT NULL, nommedecin VARCHAR(255) NOT NULL, nompatient VARCHAR(255) NOT NULL, description VARCHAR(255) NOT NULL, datedecreation DATE NOT NULL, INDEX IDX_924B326CBC6D351B (pharmacie_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE pharmacie (id INT AUTO_INCREMENT NOT NULL, nom VARCHAR(255) NOT NULL, adresse VARCHAR(255) NOT NULL, numerotelephone VARCHAR(255) NOT NULL, image VARCHAR(255) NOT NULL, note INT NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE ordonnance ADD CONSTRAINT FK_924B326CBC6D351B FOREIGN KEY (pharmacie_id) REFERENCES pharmacie (id)');
        $this->addSql('ALTER TABLE livreur ADD image VARCHAR(255) DEFAULT NULL');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE ordonnance DROP FOREIGN KEY FK_924B326CBC6D351B');
        $this->addSql('DROP TABLE ordonnance');
        $this->addSql('DROP TABLE pharmacie');
        $this->addSql('ALTER TABLE livreur DROP image');
    }
}
